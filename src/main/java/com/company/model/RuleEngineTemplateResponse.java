package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleEngineTemplateResponse {
    private String template;
    private Integer body;
    private Integer header;
    private Integer footer;
    private Integer button;
}
