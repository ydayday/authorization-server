package com.oauth2.authorization.config.provider;

import com.oauth2.authorization.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * com.oauth2.client
 * .java
 * </pre>
 *
 * @author : insung
 * @date : 2024. 3. 25.
 * @desc :  client id, secret 검증 provider
 *          ClientSecretAuthenticationProvider.class 참고
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final JdbcRegisteredClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, GlobalException {
        OAuth2ClientAuthenticationToken clientAuthentication = (OAuth2ClientAuthenticationToken)authentication;
        String cid = clientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient = clientRepository.findByClientId(cid);

        if (registeredClient == null) {
            throwInvalidClient("client_id");
        }

        if (clientAuthentication.getCredentials() == null) {
            throwInvalidClient("credentials");
        }

        if (!registeredClient.getClientAuthenticationMethods().contains(clientAuthentication.getClientAuthenticationMethod())) {
            throwInvalidClient("authentication_method");
        }

        String clientSecret = clientAuthentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
            throwInvalidClient("client_secret");
        }

        return new OAuth2ClientAuthenticationToken(registeredClient, clientAuthentication.getClientAuthenticationMethod(), clientAuthentication.getCredentials());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error("invalid_client : " + parameterName, "Client authentication failed: " + parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1");
        throw new GlobalException(error);

    }
}
