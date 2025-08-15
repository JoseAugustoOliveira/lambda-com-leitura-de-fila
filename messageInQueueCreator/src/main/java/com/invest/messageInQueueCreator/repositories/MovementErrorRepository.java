package com.invest.messageInQueueCreator.repositories;

import com.invest.messageInQueueCreator.entities.MovementError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementErrorRepository extends JpaRepository<MovementError, Long> {
}
