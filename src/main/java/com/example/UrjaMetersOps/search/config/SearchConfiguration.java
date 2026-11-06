package com.example.UrjaMetersOps.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;

//import jdk.internal.org.jline.utils.Log.info;
//import org.springframework.boot.web.client.RestTemplateBuilder;

@Slf4j
@Configuration
//@ConfigurationProperties(prefix = "search.api")
public class SearchConfiguration {



    //TODO: to move it to common package for all modules to use
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build(); // Automatically registers Jackson converters for JSON serialization
//    }

    //TODO: to move it to common package for all modules to use
    @Bean
    public RestTemplate restTemplate() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        JdkClientHttpRequestFactory factory =
                new JdkClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);

        // Register the Jackson 3 HTTP message converter
//        restTemplate.getMessageConverters().add(new JacksonJsonHttpMessageConverter());

        restTemplate.getInterceptors().add((request, body, execution) -> {

log.info("URI: {}", request.getURI());
            log.info("Method: {}", request.getMethod());
            log.info("Host: {}", request.getHeaders().getFirst("Host"));
            log.info("Origin: {}", request.getHeaders().getFirst("Origin"));
            log.info("Referer: {}", request.getHeaders().getFirst("Referer"));
            log.info("Content-Type: {}", request.getHeaders().getContentType());
            log.info("X-Signature: {}", request.getHeaders().get("X-Signature"));
            log.info("X-Timestamp: {}", request.getHeaders().get("X-Timestamp"));

            return execution.execute(request, body);
        });
        return restTemplate;

    }
}
