# Banking Transaction Processor

Runnable Spring Boot REST API kata using Java 17, Maven, Spring Data JPA, and an in-memory H2 database. No external database setup is required.

## Run in IntelliJ IDEA
1. Extract the ZIP.
2. Open the `banking-transaction-processor` folder in IntelliJ IDEA.
3. Import it as a Maven project and use JDK 17 or newer.
4. Wait for Maven dependencies to download.
5. Run `BankingTransactionProcessorApplication`.
6. Test APIs at `http://localhost:8080`.

The H2 in-memory database resets when the application stops.

## API examples

Create account:
```http
POST http://localhost:8080/api/accounts
Content-Type: application/json

{"accountId":"ACC101","initialBalance":1000.00}
```

Create another:
```http
POST http://localhost:8080/api/accounts
Content-Type: application/json

{"accountId":"ACC102","initialBalance":250.00}
```

Get account:
```http
GET http://localhost:8080/api/accounts/ACC101
```

Get balance:
```http
GET http://localhost:8080/api/accounts/ACC101/balance
```

Deposit:
```http
POST http://localhost:8080/api/accounts/ACC101/deposit
Content-Type: application/json

{"amount":500.00}
```

Withdraw:
```http
POST http://localhost:8080/api/accounts/ACC101/withdraw
Content-Type: application/json

{"amount":200.00}
```

Transfer:
```http
POST http://localhost:8080/api/transfers
Content-Type: application/json

{"fromAccountId":"ACC101","toAccountId":"ACC102","amount":200.00}
```

Transaction history:
```http
GET http://localhost:8080/api/accounts/ACC101/transactions
```

H2 console:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bankdb`
- User: `sa`
- Password: (blank)

## Run tests
In IntelliJ Maven tool window run `test`, or execute:
```bash
mvn test
```

## Notes
- `BigDecimal` is used for money.
- Deposit, withdrawal, and transfer operations are transactional.
- Pessimistic row locks protect accounts during balance-changing operations.
- Transfer ledger entries share one reference ID.
- This is an educational kata, not a production-ready banking platform. Production use also requires authentication/authorization, idempotency keys, audit controls, reconciliation, observability, and stronger operational safeguards.
