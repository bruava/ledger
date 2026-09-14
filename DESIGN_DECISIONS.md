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

## 2. Business Assumptions

**No less than zero balance allowed:**
Withdrawals cannot exceed current balance.
Withdrawing exactly the current balance is allowed.

**Amount must be strictly positive:**
Zero or negative amounts are rejected transactions.

---

## 3. Data model

**Balance is never stored:**
Balance is always calculated thought the history avoiding the need to sync.

**Transaction is immutable:**
Has a private constructor, being created by a static factory,
guaranteeing that never creates with an invalid state.
Has no setters.

**Returned transaction list is immutable:**
Returns `List.copyOf(...)` preventing external mutation of internal state.

**Amount is a BigDecimal:**
Avoids floating-point rounding errors.
Values with more than 2 decimal places are rejected avoiding precision errors.

**UUID as transaction id:**
Uniquely identifies a transaction independent of insertion order or position.
Avoids exposing sequential/guessable ids.

**Collection choice (List):**
Order matters — transaction history is inherently chronological.
No real risk of duplicates (each transaction has a unique id and timestamp).

---

## 4. Validation strategy

**Two layers, one rule:**
Format validation (type, required fields, precision) happens at the Request for a fast response.
Business rule validation (eg. insufficient balance) happens in `LedgerService` depends on current state.

**Validity checked twice, not duplicated:**
DTO layer fails fast via annotations.
`Transaction` constructor is the real guarantee, protecting even if created outside HTTP (e.g. tests).

---

## 5. Error handling

**422 for business rule violations:** 
Well-formed request violating business rules.

**400 for validation/parsing errors:** 
Missing fields, invalid enum, wrong precision.

**Generic HTTP errors:** 
Out of scope, not domain-relevant.
Use Spring's default format

**Structured error response format:**
All errors return `{ status, code, message, path, details }`.

---

## 6. Not implemented and Nice To Have

- Pagination/filtering on transaction history
- `Location` header on 201 (would need `GET /transaction/{id}`, not required)


## 7. Next possible steps

- Persistence layer (currently in-memory per requirements)
- Multi-account support with transfers
- Multi-currency support
- Proper concurrency handling for parallel requests
- Authentication / authorization
- Logging / monitoring / observability
- Containerization (e.g. Docker) for deployment

---