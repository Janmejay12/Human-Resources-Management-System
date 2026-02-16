package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.ReferalRequest;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.ReferalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/jobs")
public class ReferalController {

    private final ReferalService referalService;

    public ReferalController(ReferalService referalService) {
        this.referalService = referalService;
    }

    @PostMapping( value = "/{id}/referals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReferal(@AuthenticationPrincipal CustomEmployee user,
                                                         @Valid @RequestPart("data") ReferalRequest request,
                                                         @RequestPart("file") MultipartFile file,
                                                         @PathVariable Long id){
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
            return ResponseEntity.ok(referalService.createReferal(request,user.getUsername(),file,id));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
