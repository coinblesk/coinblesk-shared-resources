package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PayoutResponseDTO {
    @NotNull
    public final long valuePaidOut;
    @NotNull
    public final String transaction;
}
