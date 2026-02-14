package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.TravelDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelDocumentController {

    private final TravelDocumentService travelDocumentService;

    public TravelDocumentController(TravelDocumentService travelDocumentService) {
        this.travelDocumentService = travelDocumentService;
    }

    //@PreAuthorize("hasAnyRole('Employee','HR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/{id}/documents")
    public ResponseEntity<?> createTravelDocument(@AuthenticationPrincipal CustomEmployee user, @Valid @RequestPart("data") TravelDocumentRequest request,

                                                  @RequestPart("file") MultipartFile file, @PathVariable Long id){


        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // 2. Validate File Size (e.g., max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("File size exceeds 5MB");
        }

        // 3. Validate Content Type (e.g., only PDF, JPG, PNG)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png"))) {
            return ResponseEntity.badRequest().body("Unsupported file type");
        }
        try {
            return ResponseEntity.ok(travelDocumentService.createTravelDocument(request,file,user.getUsername(),id));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //@PreAuthorize("hasAnyRole('Employee','HR')")
    @GetMapping("/{id}/documents")
    public ResponseEntity<List<TravelDocumentResponse>> getAllTravelDocuments(@PathVariable Long id){
        try{
            return ResponseEntity.ok(travelDocumentService.getAllTravelDocuments(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    //@PreAuthorize("hasAnyRole('Employee','HR')")
    @GetMapping("/{travelId}/documents/{documentId}")
    public ResponseEntity<TravelDocumentResponse> getTravelDocumentById(@PathVariable long travelId, @PathVariable long documentId){
        try{
            return ResponseEntity.ok(travelDocumentService.getTravelDocumentById(travelId,documentId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

}
