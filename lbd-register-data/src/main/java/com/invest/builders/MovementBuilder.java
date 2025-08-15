package com.invest.builders;

import com.invest.entities.Address;
import com.invest.entities.Movement;
import com.invest.models.AddressDto;
import com.invest.models.enums.MovementStatus;
import com.invest.models.responses.MovementSqsResponse;

import java.util.UUID;

public class MovementBuilder {

    public static Movement movementBuild(MovementSqsResponse sqsResponse) {
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
                .address(addressBuild(sqsResponse.address()))
                .build();
    }
    private static Address addressBuild(AddressDto dto) {
        return Address.builder()
                .street(dto.street())
                .number(dto.number())
                .city(dto.city())
                .state(dto.state())
                .build();
    }
}
