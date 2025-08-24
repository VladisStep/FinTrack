package com.fintrack.transaction.controller;

import com.fintrack.transaction.dto.TransactionCreateRequest;
import com.fintrack.transaction.dto.TransactionFilterRequest;
import com.fintrack.transaction.dto.TransactionResponse;
import com.fintrack.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "CRUD for income/expense records")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public TransactionResponse create(
        @Valid @RequestBody TransactionCreateRequest req,
        Authentication auth
    ) {
        Long userId = extractUserId(auth); // реализуй согласно claim
        return service.create(userId, req);
    }

    @GetMapping
    @Operation(
        summary = "List transactions",
        description = "Returns paginated transactions filtered by date range, category, and type",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public Page<TransactionResponse> list(
        @ParameterObject TransactionFilterRequest filter,
        Authentication auth
    ) {
        Long userId = Long.parseLong(auth.getName());
        return service.find(
            userId,
            filter.page(), filter.size(),
            filter.from(), filter.to(),
            filter.categoryId(), filter.type()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete transaction", security = @SecurityRequirement(name = "bearerAuth"))
    public void delete(@PathVariable UUID id, Authentication auth) {
        Long userId = extractUserId(auth);
        service.softDelete(userId, id);
    }

    private Long extractUserId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }
}
