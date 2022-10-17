package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorRequest {
    // when there is a change in following fields ConnectorResponse fields needs to be updated.
    private String country;
    private String appName;
    private String clientReferenceNumber;
    private Object messageRequest;
}
