package com.example.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bank_transactions", indexes = {
    @Index(name = "idx_transaction_account_time", columnList = "account_number, created_at")
})
public class BankTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id", nullable = false, length = 36)
    private String referenceId;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected BankTransaction() {}

    public BankTransaction(String referenceId, String accountNumber,
                           TransactionType type, BigDecimal amount,
                           BigDecimal balanceAfter) {
        this.referenceId = referenceId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getReferenceId() { return referenceId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public Instant getCreatedAt() { return createdAt; }
}
