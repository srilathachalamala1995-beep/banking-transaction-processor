package com.example.bank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateAccountRequest(
    @NotBlank @Size(max = 50) String accountId,
    @NotNull @DecimalMin(value = "0.00") @Digits(integer = 17, fraction = 2) BigDecimal initialBalance
) {}
