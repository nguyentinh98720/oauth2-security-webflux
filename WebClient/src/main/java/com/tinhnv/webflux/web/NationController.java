package com.tinhnv.webflux.web;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.*;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.*;


@RestController
public class NationController {

    @Autowired
    WebClient webClient;

    @GetMapping("/")
    public String greeting(Authentication authentication) {
	    String currentPrincipalName = authentication.getName();

        return "Hello " + currentPrincipalName + ", welcome to client server";
    }

    @GetMapping("/regions")
    public String allRegions(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                                        OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri("/api/nation/regions")
//                .header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/regions/{id}")
    public String oneRegion(@PathVariable int id,
             @RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                                         OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri("/api/nation/regions/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    @GetMapping("/continents")
    public String allContinent(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                                   OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri("/api/nation/continents")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/continents/{id}")
    public String oneContinent(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                               OAuth2AuthorizedClient authorizedClient, @PathVariable int id) {
        return this.webClient
                .get()
                .uri("/api/nation/continents/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/countries")
    public String allCountries(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                               OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri("/api/nation/countries")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/countries/{id}")
    public String oneCountries(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                               OAuth2AuthorizedClient authorizedClient, @PathVariable int id) {
        return this.webClient
                .get()
                .uri("/api/nation/countries/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
