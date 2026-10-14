package com.example.bank.dto;

import com.example.bank.entity.Account;
import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(String accountId, BigDecimal balance, Instant createdAt) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getAccountNumber(), account.getBalance(), account.getCreatedAt());
    }
}
