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
@Named("lbd-register-data")
public class RegisterDataLambda implements RequestHandler<SQSEvent, OutputObject> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MovementService movementService;

    @Override
    public OutputObject handleRequest(SQSEvent sqsEvent, Context context) {
        log.info("RegisterDataLambda received event: {}", sqsEvent);

        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            processSingleMessage(sqsMessage);
        }

        return new OutputObject("success", context.getAwsRequestId());
    }

    private void processSingleMessage(SQSEvent.SQSMessage sqsMessage) {
        String body = sqsMessage.getBody();
        try {
            log.info("Processing message body: {}", body);
            MovementSqsResponse movement = objectMapper.readValue(body, MovementSqsResponse.class);
            movementService.persistMovement(movement, false);

        } catch (BusinessValidationException ex) {
            log.warn("Business validation failed for messageId {}: {}", sqsMessage.getMessageId(), ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error processing messageId {}: {}", sqsMessage.getMessageId(), body, ex);
            movementService.sendToRetryQueueRaw(body);
        }
    }
}
