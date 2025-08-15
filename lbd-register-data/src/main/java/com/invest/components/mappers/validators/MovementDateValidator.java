package com.invest.components.mappers.validators;

import com.invest.components.mappers.MovementValidator;
import com.invest.models.ValidationErrorDto;
import com.invest.models.responses.MovementSqsResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import static com.invest.models.constants.ValidateMessages.INVALID_DATE;

@ApplicationScoped
public class MovementDateValidator implements MovementValidator {

    @Override
    public Optional<ValidationErrorDto> validate(MovementSqsResponse movement) {
        if (movement.creationTime() != null
                && movement.updateTime() != null
                && !movement.creationTime().isBefore(movement.updateTime())) {
            return Optional.of(new ValidationErrorDto("DATES", INVALID_DATE));
        }
        return Optional.empty();
    }
}
