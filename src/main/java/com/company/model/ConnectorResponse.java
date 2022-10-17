package com.company.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConnectorResponse {
    private String uuid;
    //when there is a change in ConnectorRequest fields, following copied fields needs to be update
    private String country;
    private String clientReferenceNumber;
    private String appName;
    private Object messageRequest;
    private String requestDatetime;
    private String whatsappServiceProvider;
    private String whatsappServiceProviderUrl;
}