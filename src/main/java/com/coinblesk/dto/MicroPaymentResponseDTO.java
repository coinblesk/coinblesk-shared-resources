package com.coinblesk.dto;

import lombok.Data;

@Data
public class MicroPaymentResponseDTO {
    private final long balanceReceiver;
    private final long timeOfExecution;
}
