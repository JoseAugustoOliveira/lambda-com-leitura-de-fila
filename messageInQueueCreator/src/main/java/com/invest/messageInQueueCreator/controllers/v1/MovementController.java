package com.invest.messageInQueueCreator.controllers.v1;

import com.invest.messageInQueueCreator.models.requests.AddressUpdateRequest;
import com.invest.messageInQueueCreator.models.requests.MovementUpdateRequest;
import com.invest.messageInQueueCreator.models.responses.MovementResponse;
import com.invest.messageInQueueCreator.models.responses.PaginationResponse;
import com.invest.messageInQueueCreator.services.MovementService;
import com.invest.messageInQueueCreator.services.SqsConstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/v1/movement")
@RequiredArgsConstructor
@Tag(name = "Movement", description = "Endpoints to consult movements")
public class MovementController {

    private final MovementService movementService;
    private final SqsConstructorService sqsConstructorService;

    @GetMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Build MovementResponse using webflux")
    public Flux<MovementResponse> sendMovements() {
        return movementService.generateMovements()
                .flatMap(movement -> sqsConstructorService.sendMessage(movement).thenReturn(movement));
    }

    @GetMapping
    @Operation(summary = "List pageable movements")
    public PaginationResponse<MovementResponse> searchMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return movementService.searchMovements(page, size);
    }

    @PutMapping("/update-movement")
    @Operation(summary = "Update movement by documentNumber")
    public ResponseEntity<String> updateMovementByDocumentNumber(@Valid @RequestBody MovementUpdateRequest request) {
        movementService.updatedMovementByDocumentNumber(request);
        return ResponseEntity.ok().body("Movement updated successfully!");
    }

    @PutMapping("/update-address")
    @Operation(summary = "Update address by documentNumber")
    public ResponseEntity<String> updateAddressByDocumentNumber(@Valid @RequestBody AddressUpdateRequest request) {
        movementService.updateAddressByDocumentNumber(request);
        return ResponseEntity.ok().body("Address updated successfully!");
    }
}
