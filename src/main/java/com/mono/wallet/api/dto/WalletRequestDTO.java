package com.mono.wallet.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mono.wallet.enums.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WalletRequestDTO(
        @NotNull(message = "Wallet ID cannot be null")
        @JsonProperty("walletId")
        UUID walletId,

        @NotNull
        @JsonProperty("operationType")
        @NotNull(message = "Operation type cannot be null")
        OperationType operationType,

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
        BigDecimal amount


) {
}
