package com.company.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQSService {
    private String queueUrl = System.getenv("queue_url");
    public void publishMessage(String messageGroupId, String messageDeduplicationId, String message){
        AmazonSQS client = AmazonSQSClientBuilder.standard().build();
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageGroupId(messageGroupId)
                .withMessageDeduplicationId(messageDeduplicationId)
                .withMessageBody(message);
        try {
            client.sendMessage(sendMessageRequest);
        }catch (Exception exception){
            throw exception;
        }
    }
}
