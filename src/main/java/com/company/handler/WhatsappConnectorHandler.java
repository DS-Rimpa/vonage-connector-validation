package com.company.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.company.model.ConnectorRequest;
import com.company.model.ConnectorResponse;
import com.company.model.RuleEngineRequest;
import com.company.model.RuleEngineResponse;
import com.company.model.response.ErrorResponse;
import com.company.model.response.Response;
import com.company.service.OpenLAPIService;
import com.company.service.SQSService;
import com.company.util.CommonUtil;
import com.company.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class WhatsappConnectorHandler
        implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {
    SQSService sqsService;
    OpenLAPIService openLAPIService;
    public WhatsappConnectorHandler() {
        sqsService = new SQSService();
        openLAPIService = new OpenLAPIService();
    }
    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(
            APIGatewayV2ProxyRequestEvent requestEvent, Context context) {
        ConnectorResponse connectorResponse = null;
        try {
            String requestDatetime = CommonUtil.getCurrentDateTime();
            String uuid = UUID.randomUUID().toString();
            log.info("Request: " + requestEvent.getBody());
            ConnectorRequest request =
                    CommonUtil.getObjectMapper()
                            .readValue(requestEvent.getBody(), ConnectorRequest.class);
            if (CommonUtil.isStringNullOrEmpty(request.getAppName())
                    || CommonUtil.isStringNullOrEmpty(request.getCountry())
                    || CommonUtil.isStringNullOrEmpty(request.getClientReferenceNumber()))
                throw new RuntimeException("appName | country | clientReferenceNumber" +
                        " are mandatory fields. Cannot be null or empty");
            log.info("Invoking OpenL for finding whatsapp Provider's URL ");
            log.info("client app:{}",request.getAppName());
            RuleEngineResponse ruleEngineResponse =
                    openLAPIService.getServiceProviderDetails(
                            RuleEngineRequest.builder()
                                    .client_application(request.getAppName())
                                    .build());
            log.info(
                    "OpenL Invocation for finding whatsapp Provider's URL completed -> "
                            + ruleEngineResponse);

            populateFromNumberFromOpenLResponse(request, ruleEngineResponse);
            connectorResponse =
                    ConnectorResponse.builder()
                            .uuid(uuid)
                            .whatsappServiceProvider(ruleEngineResponse.getServiceProvider())
                            .whatsappServiceProviderUrl(ruleEngineResponse.getServiceProviderURL())
                            .requestDatetime(requestDatetime)
                            .build();
            BeanUtils.copyProperties(request, connectorResponse);
            log.info("Publishing message to SQS Queue");

            sqsService.publishMessage(
                    ruleEngineResponse.getServiceProvider(),
                    request.getClientReferenceNumber(),
                    CommonUtil.getObjectMapper().writeValueAsString(connectorResponse));
            log.info("Successfully published message to SQS Queue");
            return generateApiGatewayV2ProxyResponseEvent(
                    Response.builder()
                            .uuid(connectorResponse.getUuid())
                            .clientReferenceNumber(connectorResponse.getClientReferenceNumber())
                            .appName(connectorResponse.getAppName())
                            .status(Constants.Status.QUEUED)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            return generateApiGatewayV2ProxyResponseEvent(ErrorResponse.create(e));
        }
    }
    private void populateFromNumberFromOpenLResponse(
            ConnectorRequest request, RuleEngineResponse ruleEngineResponse) {
        if (Constants.ServiceProviders.VONAGE.equalsIgnoreCase(
                ruleEngineResponse.getServiceProvider())) {
            LinkedHashMap<String, String> vonage =
                    (LinkedHashMap<String, String>) request.getMessageRequest();
            vonage.put("from", ruleEngineResponse.getFrom());
            vonage.put("channel",Constants.CHANNEL);
            request.setMessageRequest(vonage);
        }
    }
    private APIGatewayV2ProxyResponseEvent generateApiGatewayV2ProxyResponseEvent(Response response)
            throws JsonProcessingException {
        //Create response object
        APIGatewayV2ProxyResponseEvent responseEvent = new APIGatewayV2ProxyResponseEvent();
        responseEvent.setStatusCode(HttpStatus.OK.value());
        Map<String, String> map = new HashMap<>();
        map.put("Access-Control-Allow-Headers", "Content-Type");
        map.put("Access-Control-Allow-Origin", "*");
        map.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET");
        responseEvent.setHeaders(map);
        responseEvent.setBody(CommonUtil.getObjectMapper().writeValueAsString(response));
        return responseEvent;
    }
    private APIGatewayV2ProxyResponseEvent generateApiGatewayV2ProxyResponseEvent(
            ErrorResponse response) {
        //Create response object
        APIGatewayV2ProxyResponseEvent responseEvent = new APIGatewayV2ProxyResponseEvent();
        responseEvent.setStatusCode(HttpStatus.BAD_REQUEST.value());
        Map<String, String> map = new HashMap<>();
        map.put("Access-Control-Allow-Headers", "Content-Type");
        map.put("Access-Control-Allow-Origin", "*");
        map.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET");
        responseEvent.setHeaders(map);
        try {
            responseEvent.setBody(CommonUtil.getObjectMapper().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return responseEvent;
    }
}
