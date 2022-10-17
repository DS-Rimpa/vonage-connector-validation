//package com.company;
//
//import com.company.model.RuleEngineRequest;
//import com.company.model.RuleEngineResponse;
//import com.company.service.OpenLAPIService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.web.client.HttpStatusCodeException;
//
//public class OpenlTest {
//
//    private static final String HOST = "localhost";
//    private static final int PORT = 8080;
//    private static final String PATH = "/demo.openl-tablets.org/webservice/Test%20Whatsapp%20Application/Test%20Whatsapp%20Application/WhatsappConfig";
//    private static WireMockServer wireMockServer = new WireMockServer();
//    private OpenLAPIService openLAPIService =
//            new OpenLAPIService("http://" + HOST + ":" + PORT + PATH);
//    @BeforeAll
//    static void setup() {
//        wireMockServer.start();
//        WireMock.configureFor(HOST, PORT);
//        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
//        mockResponse.withStatus(200);
//        mockResponse.withBody(
//                "{\n"
//                        + "    \"serviceProviderURL\": \"https://messages-sandbox.nexmo.com/v1/messages\",\n"
//                        + "    \"serviceProvider\": \"vonage\",\n"
//                        + "    \"from\": \"916290895296\"\n"
//                        + "}");
//        WireMock.stubFor(WireMock.post(PATH).willReturn(mockResponse));
//    }
//    static void setupNegativeScenario() {
//        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
//        mockResponse.withStatus(404);
//        mockResponse.withBody("<html><body>No service was found.</body></html>");
//        WireMock.stubFor(WireMock.post(PATH).willReturn(mockResponse));
//    }
//    @Test
//    void whenGetServiceProviderDetailsIsInvoked_thenValidResponseIsReturned()
//            throws JsonProcessingException {
//        RuleEngineRequest request =
//                RuleEngineRequest.builder().client_application("lc_whatsapp").build();
//        RuleEngineResponse response = openLAPIService.getServiceProviderDetails(request);
//        Assertions.assertNotNull(response.getServiceProvider());
//        Assertions.assertNotNull(response.getServiceProviderURL());
//        Assertions.assertNotNull(response.getFrom());
//    }
//    @Test
//    void whenGetServiceProviderDetailsIsInvoked_thenNegativeResponseIsReturned()
//            throws JsonProcessingException {
//        setupNegativeScenario();
//        RuleEngineRequest request =
//                RuleEngineRequest.builder().client_application("lc_whatsapp").build();
//        Exception ex =
//                Assertions.assertThrows(
//                        HttpStatusCodeException.class,
//                        () -> {
//                            RuleEngineResponse response =
//                                    openLAPIService.getServiceProviderDetails(request);
//                        });
//        Assertions.assertTrue(ex.getMessage().contains("404 Not Found"));
//    }
//    @AfterAll
//    static void teardown() {
//        if (null != wireMockServer && wireMockServer.isRunning()) wireMockServer.stop();
//    }
//}