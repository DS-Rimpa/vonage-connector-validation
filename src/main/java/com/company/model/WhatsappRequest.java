package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappRequest {
    private String to;
    private String from;
    private String channel;
    private String clientAppName;
    private String clientRefNo;
    private String templateName;
    private MessagePropertyContent content;
    private String locale;

}
