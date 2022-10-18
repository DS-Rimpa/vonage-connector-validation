package com.company.model;

public class RuleEngineTemplateRequest {
    private String configId;
    private String clientAppName;

    public RuleEngineTemplateRequest() {
    }

    public RuleEngineTemplateRequest(String configId, String clientAppName) {
        this.configId = configId;
        this.clientAppName = clientAppName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }
}
