package com.example.UrjaMetersOps.search.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UrjaAPIAuthService implements IAPIAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UrjaAPISession urjaAPISession;

    @Autowired
    private UrjaAPIAuthCredentials urjaAPIAuthCredentials;

    private static final String BASE_URL = "https://urja-ops.flockenergy.tech";

    @Override
    public String loginAndGetCookie() {

        String loginUrl = BASE_URL + "/login"; // Adjust path to match your exact login endpoint

        // Request body credentials
//        Map<String, String> credentials = Map.of(
//                "email", urjaAPIAuthCredentials.getEmail(),
//                "password", urjaAPIAuthCredentials.getPassword()
//        );
//
//        UrjaAPILoginRequestDto urjaAPILoginRequestDto = new UrjaAPILoginRequestDto();
//        urjaAPILoginRequestDto.setEmail(urjaAPIAuthCredentials.getEmail());
//        urjaAPILoginRequestDto.setPassword(urjaAPIAuthCredentials.getPassword());

        MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
        formBody.add("email", urjaAPIAuthCredentials.getEmail());
        formBody.add("password", urjaAPIAuthCredentials.getPassword());

        HttpHeaders headers = new HttpHeaders();

//        curl --location 'https://urja-ops.flockenergy.tech/login' \
//        --header 'accept: application/json' \
//        --header 'accept-language: en-US,en;q=0.9,hi;q=0.8' \
//        --header 'content-type: application/x-www-form-urlencoded' \
//        --header 'origin: https://urja-ops.flockenergy.tech' \
//        --header 'referer: https://urja-ops.flockenergy.tech/login' \
//        --header 'x-sveltekit-action: true' \
//        --data-urlencode 'email=operator@urja.local' \
//        --data-urlencode 'password=urja-ops-2026'

        headers.set("accept",  "application/json");
        headers.set("accept-language", "en-US,en;q=0.9,hi;q=0.8");
        headers.set("content-type", "application/x-www-form-urlencoded");
        headers.set("origin", "https://urja-ops.flockenergy.tech");
        headers.set("referer", "https://urja-ops.flockenergy.tech/login");
        headers.set("x-sveltekit-action", "true");

//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        headers.set("Content-Type", "application/json");
//        headers.set("origin", BASE_URL);
//        headers.set("x-sveltekit-action", "true");
//        headers.set("x-sveltekit-action", "true");

//        headers.set("Origin", BASE_URL);
//        headers.set("Referer", BASE_URL + "/login");
//        headers.set("Sec-Fetch-Site", "same-origin");
//        headers.set("Sec-Fetch-Mode", "cors");
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36");
//        headers.set("x-sveltekit-action", "true");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formBody, headers);

        log.info("request entity: {}", requestEntity);
//        log.info("loginrequestdto {}", urjaAPILoginRequestDto.getEmail());
//        log.info("loginrequestdto pass {}", urjaAPILoginRequestDto.getPassword());
        // Execute login request
//        ResponseEntity<Void> response = restTemplate.exchange(
//                loginUrl,
//                HttpMethod.POST,
//                requestEntity,
//                Void.class
//        );


 try {
     ResponseEntity<Void> response = restTemplate.postForEntity(
             loginUrl,
             requestEntity,
             Void.class
     );

     // Extract the Set-Cookie header from the response
     String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

     if(setCookieHeader != null) {
         urjaAPISession.setCookie(setCookieHeader);
         return setCookieHeader;
     }
 } catch (HttpClientErrorException e) {
     log.error("Status: {}", e.getStatusCode());
     log.error("Response: {}", e.getResponseBodyAsString());
 }



        throw new RuntimeException("Login failed: No SET_COOKIE header returned from server.");
    }
}
