package ru.kestar.alisabot.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.bot.sender.UserAuthenticatedMessageSender;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.service.UserService;

@Component
@RequiredArgsConstructor
public class OauthSuccessHandler implements AuthenticationSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserService userService;
    private final UserAuthenticatedMessageSender userAuthenticatedMessageSender;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        String telegramUserId = request.getParameter("state");
        if (telegramUserId != null && auth instanceof OAuth2AuthenticationToken authToken) {
            OAuth2AuthorizedClient authClient = authorizedClientService.loadAuthorizedClient(
                    authToken.getAuthorizedClientRegistrationId(), authToken.getName()
            );
            processSuccessAuthentication(telegramUserId, authClient, authToken);
        }

        response.sendRedirect("/auth/success");

    }

    private void processSuccessAuthentication(String telegramUserId,
                                              OAuth2AuthorizedClient authClient,
                                              OAuth2AuthenticationToken authToken) {
        if (authClient == null) {
            return;
        }

        final String login = Optional.ofNullable(authToken.getPrincipal().getAttribute("login"))
            .map(Object::toString)
            .orElse(null);
        final YandexTokenInfo tokenInfo = YandexTokenInfo.builder()
            .accessToken(authClient.getAccessToken().getTokenValue())
            .login(login)
            .build();
        userService.signInUser(telegramUserId, tokenInfo);
        userAuthenticatedMessageSender.send(telegramUserId, login);
    }
}
