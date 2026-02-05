package com.example.HRMS.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseDocumentId;

    private String fileName;
    private String fileType;
    private Expense expense;
    private LocalDateTime uploadDate;
    private String storageUrl;
}
