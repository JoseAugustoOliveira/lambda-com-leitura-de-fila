package com.invest.components.mappers.validators;

import com.invest.components.mappers.MovementValidator;
import com.invest.models.ValidationErrorDto;
import com.invest.models.responses.MovementSqsResponse;
import com.invest.repositories.MovementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

import static com.invest.models.constants.ValidateMessages.DUPLICATE_MOVEMENT_ID;

@ApplicationScoped
public class DuplicateMovementIdValidator implements MovementValidator {

    @Inject
    MovementRepository movementRepository;

    @Override
    public Optional<ValidationErrorDto> validate(MovementSqsResponse movement) {
        if (movementRepository.existsByIdMovement(UUID.fromString(movement.idMovement()))) {
            return Optional.of(new ValidationErrorDto("ID_MOVIMENTACAO", DUPLICATE_MOVEMENT_ID));
        }
        return Optional.empty();
    }
}
