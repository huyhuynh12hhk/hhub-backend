package shared.hub.auth.configuration;

//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import lab.start.auth_server.security.AppOAuth2GrantType;
//import lab.start.auth_server.security.passwordGrant.OAuth2PasswordGrantAuthenticationConverter;
//import lab.start.auth_server.security.passwordGrant.OAuth2PasswordGrantAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import shared.hub.auth.security.passwordGrant.OAuth2PasswordGrantAuthenticationConverter;
import shared.hub.auth.security.passwordGrant.OAuth2PasswordGrantAuthenticationProvider;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            UserDetailsService userDetailsService,
            OAuth2AuthorizationService oAuth2AuthorizationService,
            OAuth2TokenGenerator<?> tokenGenerator,
            PasswordEncoder passwordEncoder

    ) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        DelegatingAuthenticationConverter converter = new DelegatingAuthenticationConverter(List.of(
                new OAuth2AuthorizationCodeAuthenticationConverter(),     // code
                new OAuth2RefreshTokenAuthenticationConverter(),          // refresh
                new OAuth2ClientCredentialsAuthenticationConverter(),     // client_credentials
                new OAuth2PasswordGrantAuthenticationConverter()          // password
        ));

        authorizationServerConfigurer
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverter(converter)
                        .authenticationProvider(new OAuth2PasswordGrantAuthenticationProvider(
                                        userDetailsService,
                                        passwordEncoder,
                                        oAuth2AuthorizationService,
                                        tokenGenerator
                                )
                        )
                )
                .oidc(Customizer.withDefaults())
        ;
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // @formatter: off
        http
                .securityMatcher(endpointsMatcher)
                .with(authorizationServerConfigurer,
                        Customizer.withDefaults()
                )
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )

                .exceptionHandling(exception ->
                        exception
                                .defaultAuthenticationEntryPointFor(
                                        new LoginUrlAuthenticationEntryPoint("/login"),
                                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML))
                )
        ;
        // @formatter: on
        return http.build();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }


}
