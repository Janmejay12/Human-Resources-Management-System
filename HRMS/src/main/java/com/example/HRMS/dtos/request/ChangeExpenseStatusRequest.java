package com.example.HRMS.dtos.request;

import com.example.HRMS.enums.ExpenseStatus;
import com.example.HRMS.enums.Statuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeExpenseStatusRequest {
    private ExpenseStatus status;
}
