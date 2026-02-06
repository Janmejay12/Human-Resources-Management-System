package com.example.HRMS.repos;

import com.example.HRMS.entities.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, Long> {
}
