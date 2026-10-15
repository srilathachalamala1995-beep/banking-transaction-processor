package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.service.BankingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/*
 * @RestController:
 * Marks this class as a REST API controller.
 * The returned objects are converted to JSON automatically.
 *
 * @RequestMapping("/api/accounts"):
 * Defines the common URL path for all endpoints in this controller.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final BankingService bankingService;
    public AccountController(BankingService bankingService) { this.bankingService = bankingService; }

    /*
     * CREATE ACCOUNT
     *
     * POST /api/accounts
     *
     * @PostMapping: Handles HTTP POST requests.
     * @ResponseStatus(CREATED): Returns HTTP 201 when successful.
     * @Valid: Validates the request fields.
     * @RequestBody: Converts the incoming JSON into CreateAccountRequest.
     */
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        return bankingService.createAccount(request);
    }

    /*
     * GET ACCOUNT DETAILS
     *
     * GET /api/accounts/{accountId}
     *
     * @PathVariable: Reads the account ID from the URL.
     */
    @GetMapping("/{accountId}")
    public AccountResponse getAccount(@PathVariable String accountId) {
        return bankingService.getAccount(accountId);
    }

    /*
     * GET ACCOUNT BALANCE
     *
     * GET /api/accounts/{accountId}/balance
     *
     * Returns only the account balance.
     */
    @GetMapping("/{accountId}/balance")
    public BigDecimal balance(@PathVariable String accountId) {
        return bankingService.getAccount(accountId).balance();
    }
    /*
     * DEPOSIT MONEY
     *
     * POST /api/accounts/{accountId}/deposit
     *
     * @RequestBody: Receives the deposit amount as JSON.
     * @Valid: Validates the amount before calling the service.
     */
    @PostMapping("/{accountId}/deposit")
    public AccountResponse deposit(@PathVariable String accountId, @Valid @RequestBody AmountRequest request) {
        return bankingService.deposit(accountId, request.amount());
    }
    /*
     * WITHDRAW MONEY
     *
     * POST /api/accounts/{accountId}/withdraw
     *
     * The service checks the balance and performs the withdrawal.
     */
    @PostMapping("/{accountId}/withdraw")
    public AccountResponse withdraw(@PathVariable String accountId, @Valid @RequestBody AmountRequest request) {
        return bankingService.withdraw(accountId, request.amount());
    }

    /*
     * GET TRANSACTION HISTORY
     *
     * GET /api/accounts/{accountId}/transactions
     *
     * Returns the transactions associated with the specified account.
     */
    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> transactions(@PathVariable String accountId) {
        return bankingService.getTransactions(accountId);
    }
}
