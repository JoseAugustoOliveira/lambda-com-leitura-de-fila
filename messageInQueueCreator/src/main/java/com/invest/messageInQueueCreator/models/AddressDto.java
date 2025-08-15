package com.invest.messageInQueueCreator.models;

import lombok.Builder;

@Builder
public record AddressDto(
        String street,
        String number,
        String complement,
        String city,
        String postalCode,
        String state,
        String neighbourhood) {}
