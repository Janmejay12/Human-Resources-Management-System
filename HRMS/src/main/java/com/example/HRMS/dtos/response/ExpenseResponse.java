package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.ExpenseCategory;
import com.example.HRMS.enums.ExpenseStatus;
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
public class ExpenseResponse {
    private Long employeeId;
    private Long travelId;
    private String currency;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private ExpenseCategory expenseCategory;
    private ExpenseStatus expenseStatus;
}
