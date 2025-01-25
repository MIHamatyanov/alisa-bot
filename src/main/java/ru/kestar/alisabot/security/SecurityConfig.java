package ru.kestar.alisabot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String AUTH_ENDPOINT = "/oauth2/authorization";

    private final OauthRequestResolver authRequestResolver;
    private final OauthSuccessHandler authSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(endpoint -> endpoint
                    .baseUri(AUTH_ENDPOINT)
                    .authorizationRequestResolver(authRequestResolver)
                )
                .successHandler(authSuccessHandler)
            )
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
