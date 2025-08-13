package com.invest.models;

import lombok.Builder;

@Builder
public record AddressDto(
        String street,
        String number,
        String city,
        String state,
        String neighbourhood) {}
