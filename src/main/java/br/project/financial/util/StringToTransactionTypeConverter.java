package br.project.financial.util;

import br.project.financial.enums.TransactionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTransactionTypeConverter implements Converter<String, TransactionType> {
    @Override
    public TransactionType convert(String source) {
        return TransactionType.valueOf(source.trim().toUpperCase());
    }
}