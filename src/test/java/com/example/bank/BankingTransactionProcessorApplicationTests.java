package com.example.bank;

import com.example.bank.dto.*;
import com.example.bank.service.BankingService;
import com.example.bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankingTransactionProcessorApplicationTests {
    @Autowired BankingService bankingService;

    @Test
    void depositAndWithdrawUpdateBalance() {
        String id = "TEST-" + System.nanoTime();
        bankingService.createAccount(new CreateAccountRequest(id, new BigDecimal("100.00")));
        bankingService.deposit(id, new BigDecimal("50.00"));
        bankingService.withdraw(id, new BigDecimal("25.00"));
        assertEquals(0, new BigDecimal("125.00").compareTo(bankingService.getAccount(id).balance()));
        assertEquals(3, bankingService.getTransactions(id).size());
    }

    @Test
    void withdrawalCannotOverdrawAccount() {
        String id = "TEST-" + System.nanoTime();
        bankingService.createAccount(new CreateAccountRequest(id, new BigDecimal("10.00")));
        assertThrows(InsufficientFundsException.class,
            () -> bankingService.withdraw(id, new BigDecimal("11.00")));
        assertEquals(0, new BigDecimal("10.00").compareTo(bankingService.getAccount(id).balance()));
    }

    @Test
    void transferMovesFundsAndCreatesLedgerEntries() {
        String from = "FROM-" + System.nanoTime();
        String to = "TO-" + System.nanoTime();
        bankingService.createAccount(new CreateAccountRequest(from, new BigDecimal("100.00")));
        bankingService.createAccount(new CreateAccountRequest(to, new BigDecimal("20.00")));
        bankingService.transfer(new TransferRequest(from, to, new BigDecimal("30.00")));
        assertEquals(0, new BigDecimal("70.00").compareTo(bankingService.getAccount(from).balance()));
        assertEquals(0, new BigDecimal("50.00").compareTo(bankingService.getAccount(to).balance()));
        assertEquals(1, bankingService.getTransactions(from).size());
        assertEquals(1, bankingService.getTransactions(to).size());
    }
}
