package com.company.service;


import com.company.model.RuleEngineRequest;
import com.company.model.RuleEngineResponse;
import com.company.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OpenLAPIService {
    String URL = System.getenv("openl_url");
    public RuleEngineResponse getServiceProviderDetails(RuleEngineRequest ruleEngineRequest)
            throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","test");
        HttpEntity entity = new HttpEntity<>(ruleEngineRequest,headers);
        ResponseEntity<String> response;
        try {
            response =
                    restTemplate.postForEntity(
                            URL, entity, String.class);
        } catch (Exception e) {
            log.error("LCException: " + e.getMessage());
            throw e;
        }
        log.info("openl url found : {}",response);
        if (response.getStatusCode().value() == 200) {
            return CommonUtil.getObjectMapper()
                    .readValue(response.getBody(), RuleEngineResponse.class);
        }
        return null;
    }
}
