package com.fintrack.transaction.dto;

import com.fintrack.transaction.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionCreateRequest(
    @NotNull
    @Schema(
        description = "Category ID linked to this transaction",
        example = "2b4f3d48-7f26-4d2a-8e46-9f5f1f5d6e3a"
    )
    UUID categoryId,

    @NotNull
    @Schema(
        description = "Transaction type (INCOME or EXPENSE)",
        example = "EXPENSE"
    )
    TransactionType type,

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(
        description = "Transaction amount",
        example = "125.50"
    )
    BigDecimal amount,

    @NotBlank
    @Size(min = 3, max = 3)
    @Schema(
        description = "Transaction currency in ISO 4217 format",
        example = "USD"
    )
    String currency,

    @NotNull
    @Schema(
        description = "Date and time when transaction occurred (UTC)",
        example = "2025-08-18T14:05:00Z"
    )
    OffsetDateTime occurredAt,

    @Size(max = 512)
    @Schema(
        description = "Optional note about the transaction",
        example = "Groceries at Walmart"
    )
    String note
) {}
