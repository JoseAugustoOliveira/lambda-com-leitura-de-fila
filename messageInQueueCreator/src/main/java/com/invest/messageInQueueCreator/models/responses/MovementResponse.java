package com.invest.messageInQueueCreator.models.responses;

import com.invest.messageInQueueCreator.models.AddressDto;
import com.invest.messageInQueueCreator.models.enums.MovementStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MovementResponse(
        UUID idMovement,
        String name,
        String email,
        String documentNumber,
        LocalDateTime creationTime,
        LocalDateTime updateTime,
        String description,
        BigDecimal amount,
        MovementStatus movementStatus,
        LocalDate expectedDeliveryDate,
        AddressDto address) {}
