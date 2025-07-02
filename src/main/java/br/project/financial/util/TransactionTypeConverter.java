package br.project.financial.util;

import br.project.financial.enums.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public TransactionType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TransactionType.fromString(dbData);
    }
}
