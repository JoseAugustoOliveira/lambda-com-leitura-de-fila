package com.invest.messageInQueueCreator.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MOVIMENTACAO_ERRO", schema = "INVEST")
public class MovementError implements Serializable {

    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NU_CPF")
    private String documentNumber;

    @Column(name = "CAMPO")
    private String field;

    @Column(name = "MSG_ERRO")
    private String errorMessage;

    @Column(name = "JSON_BRUTO")
    private String rawJson;

    @CreationTimestamp
    @Column(name = "DT_ERRO")
    private LocalDateTime errorDate;
}
