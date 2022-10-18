package com.company.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.company.model.*;
import com.company.model.response.ErrorResponse;
import com.company.model.response.Response;
import com.company.service.OpenLAPIService;
import com.company.service.OpenlTemplateService;
import com.company.service.SQSService;
import com.company.util.CommonUtil;
import com.company.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import java.util.*;

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
            log.info("client app:{}", request.getAppName());
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

            fromNexmoContent(request);

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

    private void fromNexmoContent(ConnectorRequest request) {
        RuleEngineTemplateResponse response;
        try {
            response = new OpenlTemplateService().openlTemplateDetails(new RuleEngineTemplateRequest(request.getAppName(), request.getClientReferenceNumber()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        WhatsappRequest whatsappRequest = request.getMessageRequest();
        switch (whatsappRequest.getContent().getType()) {
            case template:
                fromTemplateContent(whatsappRequest, response);
                break;
            case text:
                if (null == whatsappRequest.getContent().getText()) {
                    throw new RuntimeException("text should not be null");
                }
                break;
            case custom:
                formCustomContent(whatsappRequest, response);
                break;
            default:
                break;
        }
    }

    private void formCustomContent(WhatsappRequest whatsappRequest, RuleEngineTemplateResponse response) {
        CustomProperty customProperty = whatsappRequest.getContent().getCustom();
        if (!customProperty.getParameters().getBody().isEmpty()) {
            if (customProperty.getParameters().getBody().size() != response.getBody()) {
                throw new RuntimeException("Parameter body count shall match");
            }
        } else {
            throw new RuntimeException("Template doesn't match");
        }
        if (!customProperty.getParameters().getHeader().isEmpty() &&
                customProperty.getParameters().getHeader().size() != response.getHeader()) {
            throw new RuntimeException("Parameter header count shall match");
        }
        if (!customProperty.getParameters().getFooter().isEmpty() &&
                customProperty.getParameters().getFooter().size() != response.getFooter()) {
            throw new RuntimeException("Parameter footer count shall match");
        }
        if (!customProperty.getParameters().getButton().isEmpty() &&
                customProperty.getParameters().getButton().size() != response.getButton()) {
            throw new RuntimeException("Parameter button count shall match");
        }
    }

    private void fromTemplateContent(WhatsappRequest whatsappRequest, RuleEngineTemplateResponse response) {
        if (whatsappRequest.getContent().getTemplate().getParameters().size() != response.getBody()) {
            throw new RuntimeException("Parameter count shall match");
        }
    }
    private void populateFromNumberFromOpenLResponse(
            ConnectorRequest request, RuleEngineResponse ruleEngineResponse) {
        if (Constants.ServiceProviders.VONAGE.equalsIgnoreCase(
                ruleEngineResponse.getServiceProvider())) {
            request.getMessageRequest().setFrom(ruleEngineResponse.getFrom());
            request.getMessageRequest().setChannel(Constants.CHANNEL);
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
