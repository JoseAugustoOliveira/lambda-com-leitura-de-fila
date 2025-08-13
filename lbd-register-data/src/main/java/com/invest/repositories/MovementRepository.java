package com.invest.repositories;

import com.invest.entities.Movement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class MovementRepository implements PanacheRepository<Movement> {

    public boolean existsByDocumentNumber(String documentNumber) {
        return find("documentNumber", documentNumber).firstResultOptional().isPresent();
    }

    public boolean existsByIdMovement(UUID idMovement) {
        return find("idMovement", idMovement).firstResultOptional().isPresent();
    }
}
