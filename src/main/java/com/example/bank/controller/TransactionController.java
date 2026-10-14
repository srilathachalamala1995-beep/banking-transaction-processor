package com.example.bank.controller;

import com.example.bank.dto.TransferRequest;
import com.example.bank.service.BankingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final BankingService bankingService;
    public TransactionController(BankingService bankingService) { this.bankingService = bankingService; }

    @PostMapping("/transfers")
    public Map<String, Object> transfer(@Valid @RequestBody TransferRequest request) {
        return bankingService.transfer(request);
    }
}
