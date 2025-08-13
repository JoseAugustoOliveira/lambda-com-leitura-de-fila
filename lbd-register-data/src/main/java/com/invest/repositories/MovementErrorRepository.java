package com.invest.repositories;

import com.invest.entities.MovementError;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovementErrorRepository implements PanacheRepository<MovementError> {
}
