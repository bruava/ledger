# Design Decisions

Full reasoning behind scope, stack, dependencies, trade-offs, etc...

---

## 1. Scope interpretation

Requires three functional capabilities:

- Record money movements (deposits and withdrawals)
- View current balance
- View transaction history

Excludes:

- Authentication / authorization
- Logging / monitoring
- Transactions / atomic operations (Interpret as distributed/DB sense)

**Guiding principle:** Keeping the solution the most light, minimalistic approach by default, 
documenting the rejected alternatives with a justification.

**Single ledger, no multi-account model:** 
No mentions to users or accounts.
A single global ledger will be implemented.

**Single currency:** 
One single not specified currency. 
Exchange rates, conversion logic, currency codes are intentionally excluded.

---

## 2. Stack: Java + Spring Boot

- Spring Boot gives a fast path to a working REST API.
- Java 21, Spring Boot 4.1.1, the current stable release at time of writing.
- Included Maven Wrapper so the project runs without requiring a pre-installed Maven (no installing software instruction).

---