package com.tinhnv.webflux.web;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.*;

import java.util.*;
import java.util.stream.*;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.*;


@RestController
public class NationController {

    @Autowired
    WebClient webClient;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping
    public String greeting(Authentication authentication) {
	    String currentPrincipalName = authentication.getName();
        String query = "select authorities.authority from authorities, users where " +
                "authorities.username = users.username and authorities.username = ?";
        String roles = "";
        try {
            roles = jdbcTemplate.queryForObject(query, String.class, currentPrincipalName);
        } catch (EmptyResultDataAccessException e) {
            return "Who are you?";
        }
        Set<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        authentication = new UsernamePasswordAuthenticationToken(currentPrincipalName, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(roles);

        return "Hello " + currentPrincipalName + ", welcome to client server";

    }

    @GetMapping("/test/regions")
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

    @GetMapping("/test/regions/{id}")
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

    @GetMapping("/test/continents")
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

    @GetMapping("/test/continents/{id}")
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

    @GetMapping("/test/countries")
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

    @GetMapping("/test/countries/{id}")
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
