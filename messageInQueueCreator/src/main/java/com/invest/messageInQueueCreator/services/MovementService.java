package com.invest.messageInQueueCreator.services;

import com.invest.messageInQueueCreator.models.AddressDto;
import com.invest.messageInQueueCreator.models.enums.MovementStatus;
import com.invest.messageInQueueCreator.models.responses.MovementResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class MovementService {

    private final Random random = new Random();

    public Flux<MovementResponse> generateMovements() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(tick -> {
                    AddressDto address = new AddressDto(
                            "Rua " + random.nextInt(100),
                            String.valueOf(random.nextInt(999)),
                            "Cidade " + random.nextInt(10),
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
}
