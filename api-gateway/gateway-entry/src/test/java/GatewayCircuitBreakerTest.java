// import org.junit.runner.RunWith;
// import org.mockserver.client.MockServerClient;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.util.TestPropertyValues;
// import org.springframework.context.ApplicationContextInitializer;
// import org.springframework.context.ConfigurableApplicationContext;
// import org.springframework.test.context.ContextConfiguration;
//
// import org.springframework.test.context.junit4.SpringRunner;
// import org.testcontainers.containers.MockServerContainer;
//
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// @RunWith(SpringRunner.class)
// @ContextConfiguration(initializers = {GatewayCircuitBreakerTest.Initializer.class})
// public class GatewayCircuitBreakerTest {
//    private static MockServerContainer mockServerContainer;
//    private static MockServerClient client ;
//    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName
//            .parse("mockserver/mockserver")
//            .withTag("mockserver-" + MockServerClient.class.getPackage().getImplementationVersion());
//
//    static {
//        mockServerContainer = new MockServerContainer();
//        mockServerContainer.();
//        client = new MockServerClient(mockServerContainer.getContainerIpAddress(),
// mockServerContainer.getServerPort());
//
//    }
//
//    static class Initializer
//            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                    "spring.cloud.gateway.routes[0].id=test-service-withResilient4j",
//                    "spring.cloud.gateway.routes[0].uri=" + mockServerContainer.getEndpoint(),
//                    "spring.cloud.gateway.routes[0].predicates[0]=" + "Path=/testService/**",
//                    "spring.cloud.gateway.routes[0].filters[0]=" + "RewritePath=/testService/(?<path>.*), /$\\{path}",
//                    "spring.cloud.gateway.routes[0].filters[1].name=" + "CircuitBreaker",
//                    "spring.cloud.gateway.routes[0].filters[1].args.name=" + "backendA",
//                    "spring.cloud.gateway.routes[0].filters[1].args.fallbackUri=" + "forward:/fallback/testService",
//                    "spring.cloud.gateway.routes[1].id=test-service-withResilient4j-statusCode",
//                    "spring.cloud.gateway.routes[1].uri=" + mockServerContainer.getEndpoint(),
//                    "spring.cloud.gateway.routes[1].predicates[0]=" + "Path=/testInternalServiceError/**",
//                    "spring.cloud.gateway.routes[1].filters[0]=" + "RewritePath=/testInternalServiceError/(?<path>.*),
// /$\\{path}",
//                    "spring.cloud.gateway.routes[1].filters[1].name=" + "CircuitBreaker",
//                    "spring.cloud.gateway.routes[1].filters[1].args.name=" + "backendB",
//                    "spring.cloud.gateway.routes[1].filters[1].args.fallbackUri=" +
// "forward:/fallback/testInternalServiceError",
//                    "spring.cloud.gateway.routes[1].filters[2]=StatusCodeCheck"
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }
//
// }
