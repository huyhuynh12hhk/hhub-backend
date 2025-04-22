import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.JsonBody.json;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import shared.hub.gateway.GatewayStartApplication;

// @Import({TestcontainersConfiguration.class, CommonTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GatewayStartApplication.class)
@Testcontainers
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GatewayCircuitBreakerTest {

    private final int breakLimit = 5;
    private final int recoveryLimit = 3;
    private final long waitDurationInOpenState = 300;

    private static final String HALF_OPEN = "HALF_OPEN";
    private static final String CLOSED = "CLOSED";
    private static final String OPEN = "OPEN";
    private static final String REALM = "test-realm-v0.1";

    @LocalServerPort
    private Integer port;

    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse("mockserver/mockserver").withTag("5.15.0"));

    @Container
    static KeycloakContainer keycloak =
            new KeycloakContainer("quay.io/keycloak/keycloak:26.1.4").withRealmImportFile("realm-export.json")

            //            .withExposedPorts(8181)
            ;

    static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {

        log.info("Start init server client...");
        mockServerClient = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
        registry.add("app.def.blog.host", mockServerContainer::getEndpoint);

        registry.add(
                "spring.security.oauth2.client.provider.keycloak.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/" + REALM);
        registry.add(
                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/" + REALM + "/protocol/openid-connect/certs");
    }

    @BeforeEach
    void setUp() {
        log.info("Start new test");
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        mockServerClient.reset();
    }

    @Test
    @Order(1)
    void shouldStart() {}

    @Test
    @Order(1)
    void shouldRunInRightFlow() {
        mockServerClient
                .when(request().withMethod("GET").withPath("/posts"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(json("""
								[]
								""")));

        var result = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/blog/posts")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .body()
                .as(List.class);

        log.info("Result length: {}", (long) result.size());
    }

    @Test
    @Order(2)
    void circuitBreaker_shouldOpenAndClose() {

        var statePath = "components.circuitBreakers.details.blog_break.details.state";

        mockServerClient
                .when(request().withMethod("GET").withPath("/posts"))
                .error(HttpError.error().withDropConnection(true));

        // Make error call to trigger circuit breaker
        var errorCount = 0;
        for (int i = 0; i < breakLimit; i++) {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/blog/posts")
                    .then();
            errorCount += 1;
            log.info("Request fail in turn: {}", errorCount);
        }

        // Verify open state.
        RestAssured.given().when().get("/actuator/health").then().log().all().body(statePath, Matchers.equalTo(OPEN));
        log.info("Circuit breaker is in state: OPEN");

        // Simulate recovery
        mockServerClient.reset();
        mockServerClient
                .when(request().withMethod("GET").withPath("/posts"))
                .respond(HttpResponse.response().withStatusCode(200));

        //        try {
        //            log.info("Server wait for {} ms",waitDurationInOpenState+50);
        //            Thread.sleep(waitDurationInOpenState+50);
        //        }catch (Exception e){
        //
        //        }

        var successCount = 1;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/blog/posts")
                .then()
                .statusCode(200);

        // Check open state is HALF OPEN
        RestAssured.given()
                .when()
                .get("/actuator/health")
                .then()
                //                .log()
                //                .all()
                .body(statePath, Matchers.equalTo(HALF_OPEN));
        log.info("Circuit breaker is in state: HALF OPEN");

        // Call enough success request to recovery
        for (int i = 0; i < recoveryLimit - 1; i++) {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/blog/posts")
                    .then()
                    //                    .log()
                    //                    .all()
                    .statusCode(200);
            successCount += 1;
            log.info("Request success in turn: {}", successCount);
        }

        // Check recovery status

        RestAssured.given().when().get("/actuator/health").then().log().all().body(statePath, Matchers.equalTo(CLOSED));
        log.info("Circuit breaker is in state: CLOSED");
    }

    @AfterClass
    public static void close() {
        mockServerContainer.close();
    }

    //    private void verifyMockServerRequest(String method, String path, int times) {
    //        mockServerClient.verify(
    //                request().withMethod(method).withPath(path),
    //                VerificationTimes.exactly(times)
    //        );
    //    }

}
