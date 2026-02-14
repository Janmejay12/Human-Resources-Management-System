package com.example.HRMS.repos;

import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, Long> {
    @Query("SELECT td FROM TravelDocument td WHERE td.travel.travelId = :travelId")
    List<TravelDocument> findAllByTravelId(@Param("travelId") Long travelId);

    @Query("SELECT td FROM TravelDocument td WHERE td.travel.travelId = :travelId AND td.travelDocumentId = :documentId")
    TravelDocument findByTravelIdAndDocumentId(@Param("travelId") Long travelId,
                                          @Param("documentId") Long documentId);
}
