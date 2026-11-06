package com.example.UrjaMetersOps.search.services;

import com.example.UrjaMetersOps.search.config.UrjaAPIAuthService;
import com.example.UrjaMetersOps.search.config.UrjaAPISession;
import com.example.UrjaMetersOps.search.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;

@Service
@Slf4j
public class UrjaMeterExportAPIClient implements IMeterExportService{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UrjaAPISession urjaAPISession;

    @Autowired
    private UrjaAPIAuthService urjaAPIAuthService;

    private static final String BASE_URL = "https://urja-ops.flockenergy.tech";


    @Override
    public List<MeterExportResponseDto> exportMeter() {
        if (urjaAPISession.getCookie() == null || urjaAPISession.getCookie().isBlank()) {
            urjaAPIAuthService.loginAndGetCookie();
        }

        try {
            log.info("using cookie {}", urjaAPISession.getCookie());
            return execute();
        } catch (HttpClientErrorException.Unauthorized ex) {
            // 3. If unauthorized (expired/invalid cookie), refresh cookie and retry ONCE
            urjaAPIAuthService.loginAndGetCookie();

            return execute();
        }
    }

    private List<MeterExportResponseDto> execute() {

        String signingSecret = getKey();
        String staticMessage = "GET\n/portal/export\npage=1";
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        String messageToSign = staticMessage + "\n" + timestamp;

        String signature = hmacSha256(messageToSign, signingSecret);

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/portal/export")
                .queryParam("page", 1)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", urjaAPISession.getCookie());
        log.info("before setting, timestamp {}", timestamp);

        log.info("before setting, signature {}", signature);
        headers.set("x-timestamp", timestamp);
        headers.set("x-signature", signature);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

//        ParameterizedTypeReference<UrjaAPIMeterExportResponseDto> responseType =
//                new ParameterizedTypeReference<>() {};
 try {
     ResponseEntity<UrjaAPIMeterExportResponseDto> response = restTemplate.exchange(
             url,
             HttpMethod.GET,
             entity,
             UrjaAPIMeterExportResponseDto.class
     );

     if(response.getBody() != null
             && response.getBody().getData() != null) {
         return response.getBody().getData();
     }
 } catch (Exception e) {
     log.error(e.getMessage());
 }



        return List.of();

    }

    private String getKey() {

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/portal/keys")
                .toUriString();

        // 2. Set up headers, including the session cookie
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", urjaAPISession.getCookie());

        // Wrap headers into an HttpEntity (body is null for a GET request)
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 3. Execute the GET request, mapping the response to an array or parameterized list
        ResponseEntity<UrjaAPIMeterExportSigningKeyResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UrjaAPIMeterExportSigningKeyResponseDto.class
        );

        // 4. Return as a List (or handle empty response safely)
        if (response.getBody() != null && response.getBody().getData() != null) {
            return response.getBody().getData().getSigningSecret();
        }

        throw new RuntimeException("Export Signing Secret could not be obtained");

    }

    private String hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC-SHA256 signature", e);
        }
    }
}
