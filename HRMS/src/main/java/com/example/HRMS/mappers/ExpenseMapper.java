package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.entities.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public static Expense toEntity(CreateExpenseRequest request) {
        if (request == null) {
            return null;
        }

        Expense expense = new Expense();


        expense.setCurrency(request.getCurrency());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setExpenseCategory(request.getExpenseCategory());
        expense.setExpenseStatus(request.getExpenseStatus());

        return expense;
    }

    // Method: Entity to DTO (Optional but recommended for response mapping)
    public static ExpenseResponse toDto(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseResponse dto = new ExpenseResponse();

        dto.setTravelId(expense.getTravel().getTravelId());
        dto.setCurrency(expense.getCurrency());
        dto.setAmount(expense.getAmount());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setExpenseCategory(expense.getExpenseCategory());
        dto.setExpenseStatus(expense.getExpenseStatus());
        dto.setExpenseId(expense.getExpenseId());
        dto.setEmployeeId(expense.getEmployee().getEmployeeId());

        return dto;
    }
}
