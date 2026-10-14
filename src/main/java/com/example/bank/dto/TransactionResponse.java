package com.example.bank.dto;

import com.example.bank.entity.*;
import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
    Long id, String referenceId, String accountId, TransactionType type,
    BigDecimal amount, BigDecimal balanceAfter, Instant timestamp
) {
    public static TransactionResponse from(BankTransaction tx) {
        return new TransactionResponse(tx.getId(), tx.getReferenceId(), tx.getAccountNumber(),
            tx.getType(), tx.getAmount(), tx.getBalanceAfter(), tx.getCreatedAt());
    }
}
