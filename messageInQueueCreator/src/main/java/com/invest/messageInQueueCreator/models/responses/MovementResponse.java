package com.invest.messageInQueueCreator.models.responses;

import com.invest.messageInQueueCreator.models.AddressDto;
import com.invest.messageInQueueCreator.models.enums.MovementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementResponse {
    private UUID idMovement;
    private String name;
    private String email;
    private String documentNumber;
    private LocalDateTime creationTime;
    private LocalDateTime updateTime;
    private String description;
    private BigDecimal amount;
    private MovementStatus movementStatus;
    private LocalDate expectedDeliveryDate;
    private AddressDto address;
}
