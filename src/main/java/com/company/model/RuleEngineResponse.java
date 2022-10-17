package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Builder
@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class RuleEngineResponse {
    private String serviceProviderURL;
    private String serviceProvider;
    private String from;
}
