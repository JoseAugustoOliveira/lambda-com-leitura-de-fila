package com.invest.messageInQueueCreator.repositories;

import com.invest.messageInQueueCreator.entities.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    Optional<Movement> findByDocumentNumber(String documentNumber);
}
