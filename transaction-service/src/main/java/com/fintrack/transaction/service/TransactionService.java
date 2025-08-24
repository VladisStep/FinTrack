package com.fintrack.transaction.service;

import com.fintrack.transaction.dto.TransactionCreateRequest;
import com.fintrack.transaction.dto.TransactionResponse;
import com.fintrack.transaction.model.Transaction;
import com.fintrack.transaction.model.TransactionType;
import com.fintrack.transaction.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public TransactionResponse create(Long userId, TransactionCreateRequest transactionCreateRequest) {
        // TODO: валидация категории через category-service (тип/принадлежность)
        Transaction transaction = Transaction.builder()
            .userId(userId)
            .categoryId(transactionCreateRequest.categoryId())
            .type(transactionCreateRequest.type())
            .amount(transactionCreateRequest.amount())
            .currency(transactionCreateRequest.currency())
            .occurredAt(transactionCreateRequest.occurredAt())
            .note(transactionCreateRequest.note())
            .build();

        Transaction savedTransaction = repo.save(transaction);
        return map(savedTransaction);
    }

    public Page<TransactionResponse> find(
        Long userId,
        Integer page, Integer size,
        OffsetDateTime from, OffsetDateTime to,
        UUID categoryId,
        TransactionType type
    ) {

        var pageable = PageRequest.of(page == null ? 0 : page, size == null ? 20 : size);

        if (from != null && to != null)
            return repo.findByUserIdAndDeletedFalseAndOccurredAtBetween(userId, from, to, pageable).map(this::map);

        if (categoryId != null)
            return repo.findByUserIdAndCategoryIdAndDeletedFalse(userId, categoryId, pageable).map(this::map);

        if (type != null)
            return repo.findByUserIdAndTypeAndDeletedFalse(userId, type, pageable).map(this::map);

        return repo.findByUserIdAndDeletedFalse(userId, pageable).map(this::map);
    }

    public void softDelete(Long userId, UUID id) {
        var tx = repo.findById(id).orElseThrow();
        if (!tx.getUserId().equals(userId)) throw new SecurityException("Forbidden");
        tx.setDeleted(true);
        repo.save(tx);
    }

    private TransactionResponse map(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getCategoryId(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getCurrency(),
            transaction.getOccurredAt(),
            transaction.getNote()
        );
    }
}