package com.invest.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MovementStatus {

    STARTED("Iniciado"),
    RECEIVED("Recebido"),
    PROCESSED("Processado"),
    POSTED("Postado"),
    ERROR("Erro");
    
    private final String value;
}
