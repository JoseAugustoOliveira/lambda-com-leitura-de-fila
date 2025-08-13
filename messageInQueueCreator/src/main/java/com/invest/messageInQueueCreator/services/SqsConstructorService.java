package com.invest.messageInQueueCreator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsConstructorService {

    private final SqsAsyncClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public Mono<Void> sendMessage(Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build();

            return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                    .doOnSuccess(response ->
                            log.info("Message sent successfully! MessageId: {}", response.messageId())
                    )
                    .doOnError(error ->
                            log.error("Failed to send message to SQS queue", error)
                    )
                    .then();
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize message to JSON", ex);
            return Mono.error(ex);
        }
    }
}
