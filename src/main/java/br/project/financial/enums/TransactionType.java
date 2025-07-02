package br.project.financial.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {
    ENTRADA,
    DESPESA,
    LUCRO,
    VENDA;

    @JsonCreator
    public static TransactionType fromString(String s) {
        return TransactionType.valueOf(s.trim().toUpperCase());
    }
}



