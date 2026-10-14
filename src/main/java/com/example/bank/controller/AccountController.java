package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.service.BankingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final BankingService bankingService;
    public AccountController(BankingService bankingService) { this.bankingService = bankingService; }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        return bankingService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(@PathVariable String accountId) {
        return bankingService.getAccount(accountId);
    }

    @GetMapping("/{accountId}/balance")
    public BigDecimal balance(@PathVariable String accountId) {
        return bankingService.getAccount(accountId).balance();
    }

    @PostMapping("/{accountId}/deposit")
    public AccountResponse deposit(@PathVariable String accountId, @Valid @RequestBody AmountRequest request) {
        return bankingService.deposit(accountId, request.amount());
    }

    @PostMapping("/{accountId}/withdraw")
    public AccountResponse withdraw(@PathVariable String accountId, @Valid @RequestBody AmountRequest request) {
        return bankingService.withdraw(accountId, request.amount());
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> transactions(@PathVariable String accountId) {
        return bankingService.getTransactions(accountId);
    }
}
