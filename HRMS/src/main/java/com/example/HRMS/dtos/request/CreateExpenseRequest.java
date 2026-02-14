package com.example.HRMS.dtos.request;

import com.example.HRMS.enums.ExpenseCategory;
import com.example.HRMS.enums.ExpenseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseRequest {

    @NotNull(message = "Travel ID is required")
    @Positive(message = "Travel ID must be a positive number")
    private Long travelId;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Expense date is required")
    @PastOrPresent(message = "Expense date cannot be in the future")
    private LocalDateTime expenseDate;

    @NotNull(message = "Expense category is required")
    private ExpenseCategory expenseCategory;

    @NotNull(message = "Expense status is required")
    private ExpenseStatus expenseStatus;

}
