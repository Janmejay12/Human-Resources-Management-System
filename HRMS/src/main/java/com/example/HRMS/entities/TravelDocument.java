package com.example.HRMS.entities;

import com.example.HRMS.enums.OwnerType;
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
public class TravelDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelDocumentId;

    private Long travelId;
    private Long employeeId;

    private String fileName;
    private String fileType;
    private OwnerType ownerType; // yet to decide the mapping
    private String uploadedBy;
    private LocalDateTime uploadDate;
    private String storageUrl;
}
