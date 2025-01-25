package ru.kestar.alisabot.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OauthRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final OAuth2AuthorizationRequestResolver delegate;

    public OauthRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
            SecurityConfig.AUTH_ENDPOINT
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest httpRequest) {
        OAuth2AuthorizationRequest authRequest = delegate.resolve(httpRequest);
        return resolve(httpRequest, authRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest httpRequest,
                                              String clientRegistrationId) {
        OAuth2AuthorizationRequest authRequest = delegate.resolve(httpRequest, clientRegistrationId);
        return resolve(httpRequest, authRequest);
    }

    private OAuth2AuthorizationRequest resolve(HttpServletRequest request, OAuth2AuthorizationRequest authRequest) {
        String telegramUserId = request.getParameter("telegramId");
        if (authRequest == null || telegramUserId == null) {
            return authRequest;
        }

        return OAuth2AuthorizationRequest.from(authRequest)
            .state(telegramUserId)
            .build();
    }
}
