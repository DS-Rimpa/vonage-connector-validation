package com.company.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String uuid;
    private String clientReferenceNumber;
    private String status;
    private String appName;
}
