package com.invest.components.mappers.validators;

import com.invest.components.mappers.MovementValidator;
import com.invest.models.ValidationErrorDto;
import com.invest.models.responses.MovementSqsResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.Optional;

import static com.invest.models.constants.ValidateMessages.AMOUNT_NEGATIVE;

@ApplicationScoped
public class NegativeAmountValidator implements MovementValidator {

    @Override
    public Optional<ValidationErrorDto> validate(MovementSqsResponse movement) {
        if (movement.amount() != null && movement.amount().compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of(new ValidationErrorDto("VALOR", AMOUNT_NEGATIVE));
        }
        return Optional.empty();
    }
}
