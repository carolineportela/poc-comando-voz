////package br.project.financial.mappers.transaction;
////
////import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
////import br.project.financial.entities.Transaction;
////import org.mapstruct.Mapper;
////
////import java.util.List;
////
////@Mapper(componentModel = "spring")
////public interface TransactionDetailedMapper {
////    TransactionDetailedDTO toDto(Transaction entity);
////    List<TransactionDetailedDTO> toDtos(List<Transaction> entities);
////}
//
//package br.project.financial.mappers.transaction;
//
//import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
//import br.project.financial.entities.Transaction;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface TransactionDetailedMapper {
//
//    @Mapping(target = "branch", source = "branch.name")
//    @Mapping(target = "costCenter", source = "costCenter.name")
//    TransactionDetailedDTO toDto(Transaction entity);
//
//    List<TransactionDetailedDTO> toDtos(List<Transaction> entities);
//}

package br.project.financial.mappers.transaction;

import br.project.financial.dtos.transaction.output.TransactionDetailedDTO;
import br.project.financial.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionDetailedMapper {

    @Mapping(target = "branch",     source = "branch.name")
    @Mapping(target = "costCenter", source = "costCenter.name")
    @Mapping(
            target = "category",
            expression = "java(entity.getItems().isEmpty() ? null : entity.getItems().get(0).getProduct().getCategory().getName())"
    )
    @Mapping(
            target = "amount",
            expression = "java(entity.getItems().isEmpty() ? null : entity.getItems().get(0).getTotalPrice())"
    )
    TransactionDetailedDTO toDto(Transaction entity);

    List<TransactionDetailedDTO> toDtos(List<Transaction> entities);
}

//
//@Mapper(componentModel = "spring")
//public interface TransactionDetailedMapper {
//
//    @Mapping(target = "branch",     source = "branch.name")
//    @Mapping(target = "costCenter", source = "costCenter.name")
//    TransactionDetailedDTO toDto(Transaction entity);
//
//    List<TransactionDetailedDTO> toDtos(List<Transaction> entities);
//}
//
