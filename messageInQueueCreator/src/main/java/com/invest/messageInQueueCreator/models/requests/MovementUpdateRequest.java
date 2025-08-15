package com.invest.messageInQueueCreator.models.requests;

import com.invest.messageInQueueCreator.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record MovementUpdateRequest(

        @NotBlank(message = "'documentNumber' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'documentNumber' must be only 11 characters")
        String documentNumber,

        @NotBlank(message = "'name' must be informed")
        String name,

        @Email
        @NotBlank(message = "'email' must be informed")
        String email,

        @NotBlank(message = "'description' must be informed")
        String description,

        @NotBlank(message = "'expectedDeliveryDate' must be informed")
        @Schema(description = "Example 2024-01-20")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate expectedDeliveryDate) {}
