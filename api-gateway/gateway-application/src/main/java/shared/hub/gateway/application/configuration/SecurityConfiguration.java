package shared.hub.gateway.application.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${app.api-prefix}")
    private String apiPrefix;

    private final String[] PUBLIC_ENDPOINTS = {"/actuator/**", "/blog/posts/list"};
    //
    //    private String[] POST_PUBLIC_ENDPOINTS = {
    //    };

    private final String[] GET_PUBLIC_ENDPOINTS = {"/blog/posts", "/blog/comments"};

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/v1/**"))
                .authorizeExchange(authorize -> authorize

                        //        .pathMatchers(HttpMethod.POST, postPublicEndpoint).permitAll()
                        .pathMatchers(HttpMethod.GET, addPrefix(GET_PUBLIC_ENDPOINTS))
                        .permitAll()
                        .pathMatchers(PUBLIC_ENDPOINTS)
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // TODO: Migrate to Keycloak client authorize policy base from gateway
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    private String[] addPrefix(String[] endpoints) {
        String[] output = new String[endpoints.length];
        for (int index = 0; index < endpoints.length; index++) {
            output[index] = apiPrefix + endpoints[index];
        }
        return output;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
