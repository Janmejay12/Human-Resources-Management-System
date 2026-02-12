package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.DocumentTypes;
import com.example.HRMS.enums.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelDocumentResponse {
    private Long travelId;
    private Long uploadedById;
    private String fileName;
    private DocumentTypes documentType;
    private OwnerType ownerType;
    private String storageUrl;
}
