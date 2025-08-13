package com.invest.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.models.responses.MovementSqsResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class SnsPublisherService {

    @Inject
    SnsClient snsClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @ConfigProperty(name = "aws.sns.queue")
    String topicArn;

    private static final String ATTRIBUTE_NAME = "event";
    private static final String ATTRIBUTE_TYPE = "String";
    private static final String ATTRIBUTE_VALUE = "PPG";

    public void publishMovement(MovementSqsResponse movement) {
        try {
            String json = objectMapper.writeValueAsString(movement);

            Map<String, MessageAttributeValue> attributes = new HashMap<>();
            attributes.put(ATTRIBUTE_NAME, MessageAttributeValue.builder()
                    .dataType(ATTRIBUTE_TYPE)
                    .stringValue(ATTRIBUTE_VALUE)
                    .build());

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(json)
                    .messageAttributes(attributes)
                    .build();

            snsClient.publish(request);
            log.info("Published message to SNS topic {}: {}", topicArn, json);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize MovementSqsResponse", e);
        } catch (SnsException e) {
            log.error("Failed to publish message to SNS", e);
        }
    }
}
