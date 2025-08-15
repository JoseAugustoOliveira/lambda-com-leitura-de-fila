package com.invest.components;

import com.invest.components.mappers.MovementValidator;
import com.invest.models.ValidationErrorDto;
import com.invest.models.responses.MovementSqsResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ApplicationScoped
public class ValidatorMovementComponent {

    @Inject
    Instance<MovementValidator> validators;

    private final AtomicInteger totalErrorMessages = new AtomicInteger();

    public void validateMovement(MovementSqsResponse movement) {
        List<ValidationErrorDto> errors = validators.stream()
                .map(v -> v.validate(movement))
                .flatMap(Optional::stream)
                .toList();

        if (!errors.isEmpty()) {
            int errorsInThisMovement = errors.size();
            int currentTotalErrors = totalErrorMessages.addAndGet(errorsInThisMovement);

            log.warn("This Movement had {} validation errors. Total validation errors so far: {}",
                    errorsInThisMovement, currentTotalErrors);
        }
    }
}
