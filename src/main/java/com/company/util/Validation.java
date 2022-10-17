package com.company.util;

import com.company.model.ConnectorRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Validation {

    public void validate(ConnectorRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String URL = "abc";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "test");
        HttpEntity entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(
                    URL, entity, String.class);
        } catch (Exception e) {
            log.error("LCException: " + e.getMessage());
            throw e;
        }

    }
}
