package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PayoutRequestDTO {
    @NotNull
    private final String publicKey;
    @NotNull
    private final String ToAddress;
}
