package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class WhatsappConfig {
    private Integer configId;
    private String clientAppName;
    private Integer bodyParameterCount;
    private Integer headerParameterCount;
    private Integer footerParameterCount;
    private Integer buttonParameterCount;
}
