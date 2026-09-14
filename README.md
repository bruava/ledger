# Tiny Ledger

Minimalistic Spring Boot ledger API — deposits, withdrawals, balance, and transaction
history.

## Prerequisites

- Java 21+ (JDK)
- Maven 3.9+ (optional if using the included wrapper)

## Running the application

From the project root, run the command for used OS.
The application starts by default on port `8082` with a base path of `/api` — e.g.
`http://localhost:8082/api/ledger/transactions`.

**Unix (macOS/Linux, or Git Bash on Windows):**

```bash
./mvnw spring-boot:run
```

**Windows (Command Prompt):**

```
mvnw.cmd spring-boot:run
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, to build a runnable jar and start it separately:

**Unix:**

```bash
./mvnw clean package
java -jar target/ledger-0.0.1-SNAPSHOT.jar
```

**Windows (Command Prompt):**

```
mvnw.cmd clean package
java -jar target\ledger-0.0.1-SNAPSHOT.jar
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean package
java -jar target\ledger-0.0.1-SNAPSHOT.jar
```
## Running the tests

**Unix (macOS/Linux, or Git Bash on Windows):**

```bash
./mvnw test
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd test
```

## Examples

> **Note:** the `curl` examples below use Unix-style syntax. On Windows, run them via
> **Git Bash** (recommended, same syntax as shown) or adapt quotes for PowerShell/cmd.

## Endpoints

| Method | Path                         | Description                     | Request body                              |
|--------|-------------------------------|----------------------------------|--------------------------------------------|
| POST   | /api/v1/ledger/transaction    | Record a deposit or withdrawal   | `{ "amount": number, "type": "DEPOSIT" \| "WITHDRAWAL" }` |
| GET    | /api/v1/ledger/balance        | Get current balance              | —                                          |
| GET    | /api/v1/ledger/transactions   | Get transaction history          | —                                          |

### 1. Record a deposit

```bash
curl -X POST http://localhost:8082/api/v1/ledger/transaction \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00, "type": "DEPOSIT"}'
```

Response `201 Created`:
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "amount": 100.00,
  "type": "DEPOSIT",
  "timestamp": "2026-09-14T07:18:32.302556Z"
}
```

### 2. Record a withdrawal

```bash
curl -X POST http://localhost:8082/api/v1/ledger/transaction \
  -H "Content-Type: application/json" \
  -d '{"amount": 30.00, "type": "WITHDRAWAL"}'
```

Response `201 Created`:
```json
{
  "id": "a1b2c3d4-1234-5678-9abc-def012345678",
  "amount": 30.00,
  "type": "WITHDRAWAL",
  "timestamp": "2026-09-14T07:18:32.302556Z"
}
```

### 3. Check current balance

```bash
curl http://localhost:8082/api/v1/ledger/balance
```

Response `200 OK`:
```json
{
  "balance": 70.00
}
```

### 4. View transaction history

```bash
curl http://localhost:8082/api/v1/ledger/transactions
```

Response `200 OK`:
```json
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "amount": 100.00,
    "type": "DEPOSIT",
    "timestamp": "2026-09-14T07:18:32.302556Z"
  },
  {
    "id": "a1b2c3d4-1234-5678-9abc-def012345678",
    "amount": 30.00,
    "type": "WITHDRAWAL",
    "timestamp": "2026-09-14T07:18:32.302557Z"
  }
]
```

### 5. Attempt a withdrawal exceeding the balance

```bash
curl -X POST http://localhost:8082/api/v1/ledger/transaction \
  -H "Content-Type: application/json" \
  -d '{"amount": 999999.00, "type": "WITHDRAWAL"}'
```

Response `422 Unprocessable Entity`:
```json
{
  "status": 422,
  "code": "INSUFFICIENT_BALANCE",
  "message": "Not enough funds to withdraw",
  "path": "/api/v1/ledger/transaction",
  "details": null
}
```

### 6. Invalid amount (negative or wrong precision)

```bash
curl -X POST http://localhost:8082/api/v1/ledger/transaction \
  -H "Content-Type: application/json" \
  -d '{"amount": -10.00, "type": "DEPOSIT"}'
```

Response `400 Bad Request`:
```json
{
  "status": 400,
  "code": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/ledger/transaction",
  "details": ["amount: Amount needs to be greater than zero"]
}
```

### 7. Invalid transaction type

```bash
curl -X POST http://localhost:8082/api/v1/ledger/transaction \
  -H "Content-Type: application/json" \
  -d '{"amount": 10.00, "type": "TRANSACTION"}'
```

Response `400 Bad Request`:
```json
{
  "status": 400,
  "code": "MALFORMED_REQUEST",
  "message": "Request body is malformed or contains an invalid value",
  "path": "/api/v1/ledger/transaction",
  "details": null
}
```

## Design decisions

See [`DESIGN_DECISIONS.md`](./DESIGN_DECISIONS.md) for the reasoning behind scope,
stack, dependencies, trade-offs, etc...