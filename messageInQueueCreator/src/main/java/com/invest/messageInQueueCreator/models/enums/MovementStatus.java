package com.invest.messageInQueueCreator.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MovementStatus {

    STARTED("Iniciado"),
    RECEIVED("Recebido"),
    PROCESSED("Processado"),
    POSTED("Postado");
    
    private final String value;
}
