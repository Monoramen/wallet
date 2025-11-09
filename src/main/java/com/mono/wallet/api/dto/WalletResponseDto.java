package com.mono.wallet.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mono.wallet.enums.OperationType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WalletResponseDto(
        @NotNull
        @JsonProperty("walletId")
        UUID walletId,

        @NotNull
        @JsonProperty("operationType")
        OperationType operationType,

        @JsonProperty("balance")
        BigDecimal balance

) {

}
