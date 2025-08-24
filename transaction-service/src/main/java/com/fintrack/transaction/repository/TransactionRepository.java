package com.fintrack.transaction.repository;

import com.fintrack.transaction.model.Transaction;
import com.fintrack.transaction.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByUserIdAndDeletedFalseAndOccurredAtBetween(
        Long userId,
        OffsetDateTime from,
        OffsetDateTime to,
        Pageable pageable
    );

    Page<Transaction> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Transaction> findByUserIdAndCategoryIdAndDeletedFalse(Long userId, UUID categoryId, Pageable pageable);

    Page<Transaction> findByUserIdAndTypeAndDeletedFalse(Long userId, TransactionType type, Pageable pageable);
}
