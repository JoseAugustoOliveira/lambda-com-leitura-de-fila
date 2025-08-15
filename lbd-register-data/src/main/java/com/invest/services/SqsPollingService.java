package com.invest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.exceptions.BusinessValidationException;
import com.invest.models.responses.MovementSqsResponse;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@ApplicationScoped
public class SqsPollingService {

    @Inject
    SqsClient sqsClient;

    // TODO: main queue
    @ConfigProperty(name = "aws.sqs.queue")
    String queueUrl;

    // TODO: retry queue
//     @ConfigProperty(name = "aws.sqs.queue.retry")
//     String queueUrl;

    @Inject
    MovementService movementService;

    @Inject
    ObjectMapper objectMapper;


    // TODO: classe onde simula a AWS para ler as filas automaticamente ao rodar a lambda
    @Scheduled(every="10s")
    void pollMessages() throws JsonProcessingException {
        var response = sqsClient.receiveMessage(r -> r.queueUrl(queueUrl).maxNumberOfMessages(10).waitTimeSeconds(5));
        for (Message msg : response.messages()) {
            try {
                log.info("Mensagem JSON recebida da fila: {}", msg.body());

                MovementSqsResponse movement = objectMapper.readValue(msg.body(), MovementSqsResponse.class);

                // TODO: main queue
                movementService.persistMovement(movement, false);

                // TODO: retry queue
                // movementService.persistMovement(movement, true);

                sqsClient.deleteMessage(d -> d.queueUrl(queueUrl).receiptHandle(msg.receiptHandle()));
            } catch (BusinessValidationException ex) {
                movementService.saveError(msg.body(), ex);
                sqsClient.deleteMessage(d -> d.queueUrl(queueUrl).receiptHandle(msg.receiptHandle()));
            }
        }
    }
}
