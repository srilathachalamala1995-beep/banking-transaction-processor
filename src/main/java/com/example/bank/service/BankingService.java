package com.example.bank.service;

import com.example.bank.dto.*;
import com.example.bank.entity.*;
import com.example.bank.exception.*;
import com.example.bank.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BankingService {
    private final AccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    public BankingService(AccountRepository accountRepository,
                          BankTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        String id = request.accountId().trim();
        if (accountRepository.findByAccountNumber(id).isPresent()) {
            throw new BadRequestException("Account ID already exists: " + id);
        }
        Account account = accountRepository.save(new Account(id, money(request.initialBalance())));
        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountId) {
        return AccountResponse.from(findAccount(accountId));
    }

    @Transactional
    public AccountResponse deposit(String accountId, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = accountRepository.findByAccountNumberForUpdate(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.setBalance(account.getBalance().add(money(amount)));
        accountRepository.save(account);
        record(UUID.randomUUID().toString(), account, TransactionType.DEPOSIT, money(amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse withdraw(String accountId, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = accountRepository.findByAccountNumberForUpdate(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
        ensureFunds(account, amount);
        account.setBalance(account.getBalance().subtract(money(amount)));
        accountRepository.save(account);
        record(UUID.randomUUID().toString(), account, TransactionType.WITHDRAWAL, money(amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public Map<String, Object> transfer(TransferRequest request) {
        validatePositiveAmount(request.amount());
        String fromId = request.fromAccountId().trim();
        String toId = request.toAccountId().trim();
        if (fromId.equals(toId)) throw new BadRequestException("Source and destination accounts must differ");

        List<Account> locked = accountRepository.findAccountsForUpdate(List.of(fromId, toId));
        Map<String, Account> byId = new HashMap<>();
        locked.forEach(a -> byId.put(a.getAccountNumber(), a));
        Account source = byId.get(fromId);
        Account destination = byId.get(toId);
        if (source == null) throw new AccountNotFoundException(fromId);
        if (destination == null) throw new AccountNotFoundException(toId);
        ensureFunds(source, request.amount());

        BigDecimal amount = money(request.amount());
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
        accountRepository.save(source);
        accountRepository.save(destination);

        String reference = UUID.randomUUID().toString();
        record(reference, source, TransactionType.TRANSFER_OUT, amount);
        record(reference, destination, TransactionType.TRANSFER_IN, amount);

        return Map.of("referenceId", reference,
            "fromAccount", AccountResponse.from(source),
            "toAccount", AccountResponse.from(destination));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(String accountId) {
        findAccount(accountId); // Return 404 for unknown account IDs.
        return transactionRepository.findByAccountNumberOrderByCreatedAtDesc(accountId)
            .stream().map(TransactionResponse::from).toList();
    }

    private Account findAccount(String accountId) {
        return accountRepository.findByAccountNumber(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Amount must be greater than zero");
        if (amount.scale() > 2)
            throw new BadRequestException("Amount must have no more than two decimal places");
    }

    private void ensureFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) throw new InsufficientFundsException();
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, java.math.RoundingMode.UNNECESSARY);
    }

    private void record(String reference, Account account, TransactionType type, BigDecimal amount) {
        transactionRepository.save(new BankTransaction(reference, account.getAccountNumber(),
            type, amount, account.getBalance()));
    }
}
