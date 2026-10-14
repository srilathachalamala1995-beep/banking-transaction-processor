package com.example.bank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank @Size(max = 50) String fromAccountId,
    @NotBlank @Size(max = 50) String toAccountId,
    @NotNull @DecimalMin(value = "0.01") @Digits(integer = 17, fraction = 2) BigDecimal amount
) {}
