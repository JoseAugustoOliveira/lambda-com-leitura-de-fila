package com.invest.components.mappers.validators;

import com.invest.components.mappers.MovementValidator;
import com.invest.models.responses.MovementSqsResponse;
import com.invest.repositories.MovementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

import static com.invest.models.constants.ValidateMessages.DUPLICATE_CPF;

@ApplicationScoped
public class DuplicateCpfValidator implements MovementValidator {

    @Inject
    MovementRepository movementRepository;

    @Override
    public Optional<String> validate(MovementSqsResponse movement) {
        if (movementRepository.existsByDocumentNumber(movement.documentNumber())) {
            return Optional.of(DUPLICATE_CPF);
        }
        return Optional.empty();
    }
}
