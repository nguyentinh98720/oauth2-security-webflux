package com.tinhnv.webflux.configuration;

import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.*;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager oauth2AuthorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction servletOAuth2AuthorizedClientExchangeFilterFunction =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(oauth2AuthorizedClientManager);
        return WebClient.builder()
                .apply(servletOAuth2AuthorizedClientExchangeFilterFunction.oauth2Configuration())
                .baseUrl("https://nation-gg.herokuapp.com")
                .build();
    }

    @Bean
    OAuth2AuthorizedClientManager oauth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository oauth2AuthorizedClientRepository) {
        OAuth2AuthorizedClientProvider oauth2AuthorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();
        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, oauth2AuthorizedClientRepository);
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(oauth2AuthorizedClientProvider);
        return defaultOAuth2AuthorizedClientManager;
    }
}
