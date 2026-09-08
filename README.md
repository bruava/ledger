# Tiny Ledger

Minimalistic Spring Boot ledger API — deposits, withdrawals, balance, and transaction
history.

## Prerequisites

- Java 21+ (JDK)
- Maven 3.9+ (optional if using the included wrapper)

## Running the application

From the project root, run the command for your OS:

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

The application starts by default on port `8082` with a base path of `/api` — e.g.
`http://localhost:8082/api/transactions`.

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

## Examples

TODO: Add examples

## Design decisions

See [`DESIGN_DECISIONS.md`](./DESIGN_DECISIONS.md) for the reasoning behind scope,
stack, dependencies, trade-offs, etc...