package com.invest.messageInQueueCreator.models.requests;

import com.invest.messageInQueueCreator.utils.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AddressUpdateRequest(

        @NotBlank(message = "'documentNumber' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'documentNumber' must be only 11 characters")
        String documentNumber,

        @NotBlank(message = "'street' must be informed")
        String street,

        @NotBlank(message = "'number' must be informed")
        @Pattern(regexp = Regex.ONLY_NUMBER, message = "'number' must be only numbers")
        String number,

        @NotBlank(message = "'city' must be informed")
        String city,

        @NotBlank(message = "'state' must be informed")
        @Pattern(regexp = Regex.STATE, message = "'state' must be only 2 characters")
        String state,

        @NotBlank(message = "'postalCode' must be informed")
        @Pattern(regexp = Regex.POSTAL_CODE, message = "'postalCode' must be a valid with 8 characters")
        String postalCode,

        String complement,

        @NotBlank(message = "'neighbourhood' must be informed")
        String neighbourhood) {}
