package com.kiptoo.cool.bank.api.mapper;

import com.kiptoo.cool.bank.api.dtos.TransactionResponseDto;
import com.kiptoo.cool.bank.api.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDto toDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getTransactionReference(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null,
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getId() : null,
                transaction.getUser().getId()
        );
    }
}