package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @ManyToOne()
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @CreationTimestamp
    private LocalDateTime uploadDate;

    @Column(name = "storage_url", nullable = false, length = 512)
    private String storageUrl;
}
