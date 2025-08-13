package com.invest.entities;

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

import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ENDERECO", schema = "INVEST")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PK")
    private Long id;

    @Column(name = "RUA")
    private String street;

    @Column(name = "NUMERO")
    private String number;

    @Column(name = "CIDADE")
    private String city;

    @Column(name = "ESTADO")
    private String state;

    @Column(name = "BAIRRO")
    private String neighbourhood;
}
