package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.entities.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMappper {

    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "expenseDate", source = "expenseDate")
    @Mapping(target = "expenseCategory", source = "expenseCategory")
    @Mapping(target = "expenseStatus", source = "expenseStatus")
    ExpenseResponse toDto(Expense expense);

    Expense toEntity(CreateExpenseRequest request);

}
