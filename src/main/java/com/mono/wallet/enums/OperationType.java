package com.mono.wallet.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.JsonParseException;


import java.util.Arrays;

public enum OperationType {
    DEPOSIT,
    WITHDRAW;


    @JsonCreator
    public static OperationType fromString(String value) throws InvalidFormatException {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException(
                    null,
                    String.format("Invalid operation type: '%s'", value),
                    value,
                    OperationType.class
            );
        }
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }


}
