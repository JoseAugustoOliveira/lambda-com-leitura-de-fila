package com.invest.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.invest.models.AddressDto;
import com.invest.models.enums.MovementStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MovementSqsResponse(
        String idMovement,
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
