package com.example.HRMS.repos;

import com.example.HRMS.entities.ExpenseDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDocumentRepository extends JpaRepository<ExpenseDocument, Long> {
}
