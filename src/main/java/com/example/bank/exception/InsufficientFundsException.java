package com.example.bank.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() { super("Insufficient balance"); }
}
