package shared.hub.auth.security.passwordGrant;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static shared.hub.auth.security.AppOAuth2GrantType.PASSWORD_GRANT_TYPE;


@RequiredArgsConstructor
public class OAuth2PasswordGrantAuthenticationProvider implements AuthenticationProvider {
    //    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final Log logger = LogFactory.getLog(getClass());
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2PasswordGrantAuthenticationToken passwordGrantAuthenticationToken = (OAuth2PasswordGrantAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                getAuthenticatedClientElseThrowInvalidClient(passwordGrantAuthenticationToken);


        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes().contains(passwordGrantAuthenticationToken.getGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        Set<String> authorizedScopes = Collections.emptySet();
        if (passwordGrantAuthenticationToken.getScopes() != null) {
            for (String requestedScope : passwordGrantAuthenticationToken.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(passwordGrantAuthenticationToken.getScopes());
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Checking user credentials");
        }

        String providedUsername = passwordGrantAuthenticationToken.getUsername();
        String providedPassword = passwordGrantAuthenticationToken.getPassword();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(providedUsername);
        if (!this.passwordEncoder.matches(providedPassword, userDetails.getPassword())) {
            throw new OAuth2AuthenticationException("Invalid resource owner credentials");
        }

        var userPrincipal = new UsernamePasswordAuthenticationToken(
                userDetails, providedPassword, userDetails.getAuthorities());


        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(userPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(PASSWORD_GRANT_TYPE)
                .authorizationGrant(passwordGrantAuthenticationToken)
                .put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));


        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();

        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.",
                    "BAD_CREDENTIALS_REQUEST_ERROR_URI");
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Generated access token");
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (generatedRefreshToken != null) {
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate a valid refresh token.", "BAD_CREDENTIALS_REQUEST_ERROR_URI");
                    throw new OAuth2AuthenticationException(error);
                }

                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Generated refresh token");
                }

                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            }
        }


        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating authorization");
        }

        Map<String, Object> tokenMetadata = new HashMap<>();
        tokenMetadata.put("username", userDetails.getUsername());
        tokenMetadata.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        if (!authorizedScopes.isEmpty()) {
            tokenMetadata.put("scopes", authorizedScopes);
        }

        OAuth2Authorization authorization = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(userDetails.getUsername())
                .authorizationGrantType(PASSWORD_GRANT_TYPE)
                .authorizedScopes(authorizedScopes)

                // Attach access token
                .token(generatedAccessToken, metadata ->
                        metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, tokenMetadata)
                )
                // Attach refresh token
                .refreshToken(refreshToken)
                .build();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Saving authorization");
        }

        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,
                clientPrincipal,
                accessToken,
                refreshToken
        );
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}