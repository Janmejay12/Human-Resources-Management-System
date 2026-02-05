package com.example.HRMS.entities;

import com.example.HRMS.enums.ExpenseCategory;
import com.example.HRMS.enums.ExpenseStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    private Travel travel;
    private List<ExpenseDocument> expenseDocuments;
    private Employee employee;
    private String description;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private ExpenseCategory expenseCategory;
    private ExpenseStatus expenseStatus;
    private String hrRemarks;

}
