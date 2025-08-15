package com.invest.messageInQueueCreator.repositories;

import com.invest.messageInQueueCreator.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
