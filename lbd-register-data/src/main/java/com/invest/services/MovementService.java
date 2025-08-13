package com.invest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.components.ValidatorMovementComponent;
import com.invest.entities.Address;
import com.invest.entities.Movement;
import com.invest.entities.MovementError;
import com.invest.exceptions.BusinessValidationException;
import com.invest.models.enums.MovementStatus;
import com.invest.models.responses.MovementSqsResponse;
import com.invest.repositories.MovementErrorRepository;
import com.invest.repositories.MovementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MovementService {

    @Inject
    SqsClient sqsClient;

    @ConfigProperty(name = "aws.sqs.queue.retry")
    String retryQueueUrl;

    @Inject
    ObjectMapper objectMapper;

    private final MovementRepository movementRepository;
    private final MovementErrorRepository movementErrorRepository;
    private final ValidatorMovementComponent validatorMovementComponent;

    @Transactional
    public void persistMovement(MovementSqsResponse movementResponse, boolean fromRetry) {
        try {

            validatorMovementComponent.validateMovement(movementResponse);

            var movement = buildMovement(movementResponse);
            movementRepository.persist(movement);

            movement.setMovementStatus(MovementStatus.PROCESSED);
            movementRepository.persist(movement);

            log.info("Movement processed successfully. Id: {}", movementResponse.idMovement());

        } catch (BusinessValidationException ex) {
            if (!fromRetry) {
                sendToRetryQueue(movementResponse);
            }
            log.warn("Validation failed for Id {}: {}", movementResponse.idMovement(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void saveError(String rawJson, Exception ex) {
        try {
            MovementSqsResponse dto = objectMapper.readValue(rawJson, MovementSqsResponse.class);
            String documentNumber = dto.documentNumber();

            MovementError error = MovementError.builder()
                    .documentNumber(documentNumber)
                    .field(ex instanceof BusinessValidationException ? ((BusinessValidationException) ex).getField() : null)
                    .errorMessage(ex.getMessage())
                    .rawJson(rawJson)
                    .build();

            movementErrorRepository.persist(error);
            log.info("Successfully resisted movement error in the error table.");
        } catch (JsonProcessingException jsonEx) {
            log.error("Fail to converter JSON to save error. RawJson: {}", rawJson, jsonEx);
        }
    }

    public void sendToRetryQueue(MovementSqsResponse movementResponse) {
        try {
            String messageBody = objectMapper.writeValueAsString(movementResponse);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(retryQueueUrl)
                    .messageBody(messageBody)
                    .build());
            log.info("Message sent to retry queue: {}", retryQueueUrl);
        } catch (Exception e) {
            log.error("Failed to send message to retry queue", e);
        }
    }

    public void sendToRetryQueueRaw(String rawJson) {
        try {
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(retryQueueUrl)
                    .messageBody(rawJson)
                    .build());
            log.info("Raw message sent to retry queue: {}", retryQueueUrl);
        } catch (Exception e) {
            log.error("Failed to send raw message to retry queue", e);
        }
    }

    private Movement buildMovement(MovementSqsResponse sqsResponse) {
        return Movement.builder()
                .idMovement(UUID.fromString(sqsResponse.idMovement()))
                .name(sqsResponse.name())
                .email(sqsResponse.email())
                .description(sqsResponse.description())
                .documentNumber(sqsResponse.documentNumber())
                .creationTime(sqsResponse.creationTime())
                .amount(sqsResponse.amount())
                .movementStatus(MovementStatus.RECEIVED)
                .expectedDeliveryDate(sqsResponse.expectedDeliveryDate())
                .address(Address.builder()
                        .street(sqsResponse.address().street())
                        .number(sqsResponse.address().number())
                        .city(sqsResponse.address().city())
                        .state(sqsResponse.address().state())
                        .neighbourhood(sqsResponse.address().neighbourhood())
                        .build())
                .build();
    }
}
