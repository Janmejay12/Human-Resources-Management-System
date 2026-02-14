package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.entities.TravelDocument;
import org.springframework.stereotype.Component;

@Component
public class TravelDocumentMapper {
    public static TravelDocument toEntity(TravelDocumentRequest request){
        if (request == null) {
            return null;
        }
        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setDocumentType(request.getDocumentType());
        travelDocument.setFileName(request.getFileName());
        travelDocument.setOwnerType(request.getOwnerType());
        return travelDocument;
    }

    public static TravelDocumentResponse toDto(TravelDocument travelDocument){
        if (travelDocument == null) {
            return null;
        }
        TravelDocumentResponse travelDocumentResponse = new TravelDocumentResponse();
        travelDocumentResponse.setUploadedById(travelDocument.getUploadedBy().getEmployeeId());
        travelDocumentResponse.setTravelId(travelDocument.getTravel().getTravelId());
        travelDocumentResponse.setDocumentType(travelDocument.getDocumentType());
        travelDocumentResponse.setOwnerType(travelDocument.getOwnerType());
        travelDocumentResponse.setStorageUrl(travelDocument.getStorageUrl());
        travelDocumentResponse.setFileName(travelDocument.getFileName());
        return travelDocumentResponse;
    }
}
