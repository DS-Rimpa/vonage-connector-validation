package com.company.service;

import com.company.model.RuleEngineTemplateRequest;
import com.company.model.RuleEngineTemplateResponse;
import com.company.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class OpenlTemplateService {
    String URL = System.getenv("1");
    public RuleEngineTemplateResponse openlTemplateDetails(RuleEngineTemplateRequest ruleEngineTemplateRequest)
            throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","test");
        HttpEntity entity = new HttpEntity<>(ruleEngineTemplateRequest,headers);
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
                    .readValue(response.getBody(), RuleEngineTemplateResponse.class);
        }
        return null;
    }
}
