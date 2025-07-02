package br.project.financial.mappers.transaction;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionStructMapper {
    TransactionStructMapper INSTANCE = Mappers.getMapper(TransactionStructMapper.class);
}
