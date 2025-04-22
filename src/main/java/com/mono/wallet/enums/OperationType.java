package com.mono.wallet.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {
    DEPOSIT,
    WITHDRAW;


    @JsonCreator
    public static OperationType fromString(String value) {
        try {
            return OperationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid operation type: " + value);
        }
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
