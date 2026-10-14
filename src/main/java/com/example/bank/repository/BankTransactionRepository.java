package com.example.bank.repository;

import com.example.bank.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
    List<BankTransaction> findByAccountNumberOrderByCreatedAtDesc(String accountNumber);
}
