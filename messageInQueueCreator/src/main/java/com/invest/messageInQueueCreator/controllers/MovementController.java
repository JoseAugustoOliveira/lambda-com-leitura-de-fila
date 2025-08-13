package com.invest.messageInQueueCreator.controllers;

import com.invest.messageInQueueCreator.models.responses.MovementResponse;
import com.invest.messageInQueueCreator.services.MovementService;
import com.invest.messageInQueueCreator.services.SqsConstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class MovementController {

    private final MovementService movementService;
    private final SqsConstructorService sqsConstructorService;

    @GetMapping(value = "/movement/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovementResponse> sendMovements() {
        return movementService.generateMovements()
                .flatMap(movement -> sqsConstructorService.sendMessage(movement).thenReturn(movement));
    }
}
