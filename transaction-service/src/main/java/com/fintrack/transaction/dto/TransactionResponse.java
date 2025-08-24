package com.fintrack.transaction.dto;

import com.fintrack.transaction.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponse(

    @Schema(
        description = "Transaction unique identifier",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    UUID id,

    @Schema(
        description = "Category ID linked to this transaction",
        example = "2b4f3d48-7f26-4d2a-8e46-9f5f1f5d6e3a"
    )
    UUID categoryId,

    @Schema(
        description = "Transaction type (INCOME or EXPENSE)",
        example = "EXPENSE"
    )
    TransactionType type,

    @Schema(
        description = "Transaction amount",
        example = "125.50"
    )
    BigDecimal amount,

    @Schema(
        description = "Transaction currency in ISO 4217 format",
        example = "USD"
    )
    String currency,

    @Schema(
        description = "Date and time when transaction occurred (UTC)",
        example = "2025-08-18T14:05:00Z"
    )
    OffsetDateTime occurredAt,

    @Schema(
        description = "Optional note about the transaction",
        example = "Groceries at Walmart"
    )
    String note
) {}

