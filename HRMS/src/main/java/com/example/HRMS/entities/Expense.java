package com.example.HRMS.entities;

import com.example.HRMS.enums.ExpenseCategory;
import com.example.HRMS.enums.ExpenseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToOne()
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpenseDocument> expenseDocuments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    private String description;
    @Column(nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.DATE)
    private LocalDateTime expenseDate;

    @Column(nullable = false)
    private ExpenseCategory expenseCategory;

    @ManyToOne()
    @JoinColumn(name = "expense_status_id",nullable = false)
    private ExpenseStatus expenseStatus;

    @Column(name = "hr_remarks", nullable = false)
    private String hrRemarks;

}
