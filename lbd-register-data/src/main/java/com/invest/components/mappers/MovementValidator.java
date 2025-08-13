package com.invest.components.mappers;

import com.invest.models.responses.MovementSqsResponse;

import java.util.Optional;

@FunctionalInterface
public interface MovementValidator {
    Optional<String> validate(MovementSqsResponse movement);
}
