package shared.hub.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import shared.hub.auth.model.entity.AppUser;
import shared.hub.auth.repository.AppUserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class AuthServerApplicationTests {

	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private AppUserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

//	@Container
//	static MockServerContainer mockServerContainer = new MockServerContainer(
//			DockerImageName.parse("mockserver/mockserver").withTag("5.15.0"));


//	static MockServerClient mockServerClient;
//
//	@DynamicPropertySource
//	static void overrideProperties(DynamicPropertyRegistry registry) {
//
//		log.info("Start init server client...");
//		mockServerClient = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
//		registry.add("app.services.profile", mockServerContainer::getEndpoint);
//
//	}

	private static AppUser user1 = AppUser.builder()
			.fullName("Test User One")
			.email("usertest01@example.com")
			.username("utest01")
			.password(passwordEncoder.encode("password"))
			.build();

	@BeforeEach
	public void init(){
		userRepository.save(user1);
	}

	@AfterEach
	public void clean(){
		userRepository.delete(user1);
	}

	@Test
//	@Order(1)
	void testUserShouldLoginWithPasswordGrantSuccess() throws Exception {
		this.mockMvc.perform(post("/oauth2/token")
						.param("grant_type", "password")
						.param("scope", "openid")
						.param("client_id", "test-client")
						.param("client_secret", "secret")
						.param("username", "utest01")
						.param("password","password")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").isString())
				.andExpect(jsonPath("$.expires_in").isNumber())
				.andExpect(jsonPath("$.scope").value("openid"))
				.andExpect(jsonPath("$.token_type").value("Bearer"));
	}

}
