package com.example.HRMS.entities;

import com.example.HRMS.enums.DocumentTypes;
import com.example.HRMS.enums.OwnerType;
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
public class TravelDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelDocumentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uploadedby_id", nullable = false)
    private Employee uploadedBy;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "document_type")
    private DocumentTypes documentType; // ***

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType; // yet to decide the mapping

    @CreationTimestamp
    private LocalDateTime uploadTime;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "storage_url", nullable = false, length = 512)
    private String storageUrl;
}
