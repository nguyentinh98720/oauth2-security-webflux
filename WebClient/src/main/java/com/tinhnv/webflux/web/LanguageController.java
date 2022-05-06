package com.tinhnv.webflux.web;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.*;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequestMapping("/languages")
public class LanguageController {
    @Autowired
    private WebClient webClient;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<String> allLanguages(@RegisteredOAuth2AuthorizedClient("nation-client-authorization-code")
                                               OAuth2AuthorizedClient client, @RequestParam("pageNo") int pageNo,
                                               @RequestParam("pageSize") int pageSize) {
        String result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/nation-manage/languages/list")
                        .queryParam("pageNo", pageNo)
                        .queryParam("pageSize", pageSize)
                        .build())
                .attributes(oauth2AuthorizedClient(client))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }
}