package com.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RuleEngineRequest {
    @JsonProperty("client_application")
    private String client_application;
}
