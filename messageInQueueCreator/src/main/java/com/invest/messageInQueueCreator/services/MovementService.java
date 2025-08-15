package com.invest.messageInQueueCreator.services;

import com.invest.messageInQueueCreator.entities.Address;
import com.invest.messageInQueueCreator.entities.Movement;
import com.invest.messageInQueueCreator.models.AddressDto;
import com.invest.messageInQueueCreator.models.enums.MovementStatus;
import com.invest.messageInQueueCreator.models.requests.AddressUpdateRequest;
import com.invest.messageInQueueCreator.models.requests.MovementUpdateRequest;
import com.invest.messageInQueueCreator.models.responses.MovementResponse;
import com.invest.messageInQueueCreator.models.responses.PaginationResponse;
import com.invest.messageInQueueCreator.repositories.AddressRepository;
import com.invest.messageInQueueCreator.repositories.MovementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementService {

    private final Random random = new Random();

    private final AddressRepository addressRepository;
    private final MovementRepository movementRepository;

    public Flux<MovementResponse> generateMovements() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(tick -> {
                    AddressDto address = new AddressDto(
                            "Rua " + random.nextInt(100),
                            String.valueOf(random.nextInt(999)),
                            "Complemento " + random.nextInt(10),
                            "Cidade " + random.nextInt(10),
                            String.format("%08d", random.nextInt(100_000_000)),
                            "SP",
                            "Bairro " + random.nextInt(5)
                    );

                    return new MovementResponse(
                            UUID.randomUUID(),
                            "User " + UUID.randomUUID().toString().substring(0, 5),
                            "email " + random.nextInt(10) + "@email.com",
                            String.format("%011d", Math.abs(random.nextLong() % 100000000000L)),
                            LocalDateTime.now(),
                            null,
                            "Automatically generated movement",
                            BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, BigDecimal.ROUND_HALF_UP),
                            MovementStatus.STARTED,
                            LocalDate.now().plusDays(random.nextInt(10)),
                            address
                    );
                });
    }

    public PaginationResponse<MovementResponse> searchMovements(int page, int size) {
        Page<Movement> movementsPage = movementRepository.findAll(PageRequest.of(page, size));

        List<MovementResponse> response = movementsPage.stream()
                .map(this::buildMovementResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(response, movementsPage.getTotalElements(), page, size);
    }

    public void updatedMovementByDocumentNumber(MovementUpdateRequest request) {
        Optional<Movement> optionalMovement = movementRepository.findByDocumentNumber(request.documentNumber());

        if (optionalMovement.isEmpty()) {
            throw new EntityNotFoundException("Movement not found for this documentNumber" + request.documentNumber());
        }

        Movement movementToUpdate = optionalMovement.get();

        movementToUpdate.setDescription(request.description());
        movementToUpdate.setName(request.name());
        movementToUpdate.setExpectedDeliveryDate(request.expectedDeliveryDate());
        movementToUpdate.setEmail(request.email());

        movementRepository.save(movementToUpdate);

        ResponseEntity.ok().build();
    }

    public void updateAddressByDocumentNumber(AddressUpdateRequest request) {
        Optional<Movement> optionalMovement = movementRepository.findByDocumentNumber(request.documentNumber());

        if (optionalMovement.isEmpty()) {
            throw new EntityNotFoundException("Movement not found for this documentNumber" + request.documentNumber());
        }

        Movement movementToUpdate = optionalMovement.get();

        Address addressToUpdate = movementToUpdate.getAddress();
        if (addressToUpdate == null) {
            addressToUpdate = new Address();
            movementToUpdate.setAddress(addressToUpdate);
        }

        addressToUpdate.setCity(request.city());
        addressToUpdate.setNumber(request.number());
        addressToUpdate.setComplement(request.complement());
        addressToUpdate.setNeighbourhood(request.neighbourhood());
        addressToUpdate.setState(request.state());
        addressToUpdate.setPostalCode(request.postalCode());
        addressToUpdate.setStreet(request.street());

        movementRepository.save(movementToUpdate);
    }

    private MovementResponse buildMovementResponse(Movement movement) {
        return MovementResponse.builder()
                .idMovement(movement.getIdMovement())
                .name(movement.getName())
                .email(movement.getEmail())
                .documentNumber(movement.getDocumentNumber())
                .creationTime(movement.getCreationTime())
                .updateTime(movement.getUpdateTime())
                .description(movement.getDescription())
                .amount(movement.getAmount())
                .movementStatus(movement.getMovementStatus())
                .expectedDeliveryDate(movement.getExpectedDeliveryDate())
                .address(buildAddressDto(movement.getAddress()))
                .build();
    }

    private AddressDto buildAddressDto(Address address) {
        return AddressDto.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .city(address.getCity())
                .state(address.getState())
                .neighbourhood(address.getNeighbourhood())
                .complement(address.getComplement())
                .postalCode(address.getPostalCode())
                .build();
    }
}
