package com.fintrack.transaction.dto;

import com.fintrack.transaction.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionFilterRequest(

    @Schema(description = "Start date (inclusive)", example = "2025-08-01T00:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  // на всякий случай
    OffsetDateTime from,

    @Schema(description = "End date (inclusive)", example = "2025-08-31T23:59:59Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime to,

    @Schema(description = "Category ID", example = "2b4f3d48-7f26-4d2a-8e46-9f5f1f5d6e3a")
    UUID categoryId,

    @Schema(description = "Transaction type (INCOME/EXPENSE)", example = "EXPENSE")
    TransactionType type,

    @Schema(description = "Page number (0-based)", example = "0")
    Integer page,

    @Schema(description = "Page size", example = "20")
    Integer size
) {}


