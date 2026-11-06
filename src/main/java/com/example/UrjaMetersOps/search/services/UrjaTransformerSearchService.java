package com.example.UrjaMetersOps.search.services;

import com.example.UrjaMetersOps.search.config.UrjaAPIAuthService;
import com.example.UrjaMetersOps.search.config.UrjaAPISession;
import com.example.UrjaMetersOps.search.dtos.TransformerResponseDto;
import com.example.UrjaMetersOps.search.dtos.UrjaAPITransformerSearchResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
public class UrjaTransformerSearchService implements ITransformerSearchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UrjaAPISession urjaAPISession;

    @Autowired
    private UrjaAPIAuthService urjaAPIAuthService;

    private static final String BASE_URL = "https://urja-ops.flockenergy.tech";

    @Override
    public List<TransformerResponseDto> listTransformers(int page) {

        if (urjaAPISession.getCookie() == null || urjaAPISession.getCookie().isBlank()) {
            urjaAPIAuthService.loginAndGetCookie();
        }

        try {
            log.info("using cookie {}", urjaAPISession.getCookie());
            return execute(page);
        } catch (HttpClientErrorException.Unauthorized ex) {
            // 3. If unauthorized (expired/invalid cookie), refresh cookie and retry ONCE
            urjaAPIAuthService.loginAndGetCookie();

            return execute(page);
        }
    }

    private List<TransformerResponseDto> execute(int page) {

// 1. Build the target URL safely with query parameters using UriComponentsBuilder
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/portal/dts")
                .queryParam("page", page)
                .toUriString();

        // 2. Set up headers, including the session cookie
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", urjaAPISession.getCookie());

        // Wrap headers into an HttpEntity (body is null for a GET request)
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 3. Execute the GET request, mapping the response to an array or parameterized list
        ResponseEntity<UrjaAPITransformerSearchResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UrjaAPITransformerSearchResponseDto.class
        );

        // 4. Return as a List (or handle empty response safely)
        if (response.getBody() != null && response.getBody().getData() != null) {
            return response.getBody().getData();
        }

        return List.of();
    }
}
