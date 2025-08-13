package com.invest.infrastructure;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invest.exceptions.BusinessValidationException;
import com.invest.models.OutputObject;
import com.invest.models.responses.MovementSqsResponse;
import com.invest.services.MovementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Named("lbd-register-data-retry")
public class RetryLambda implements RequestHandler<SQSEvent, OutputObject> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MovementService movementService;

    @Override
    public OutputObject handleRequest(SQSEvent sqsEvent, Context context) {
        log.info("RetryLambda received event: {}", sqsEvent);

        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            processRetryMessage(sqsMessage);
        }

        return new OutputObject("success", context.getAwsRequestId());
    }

    private void processRetryMessage(SQSEvent.SQSMessage sqsMessage) {
        String body = sqsMessage.getBody();
        try {
            MovementSqsResponse movement = objectMapper.readValue(body, MovementSqsResponse.class);
            movementService.persistMovement(movement, true);

        } catch (BusinessValidationException ex) {
            log.warn("Retry validation failed, saving to error table. MessageId {}: {}", sqsMessage.getMessageId(), ex.getMessage());
            movementService.saveError(body, ex);

        } catch (Exception ex) {
            log.error("Unexpected error in retry for messageId {}. Saving to error table.", sqsMessage.getMessageId(), ex);
            movementService.saveError(body, ex);
        }
    }
}
