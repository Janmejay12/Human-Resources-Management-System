package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.CreateExpenseDocumentRequest;
import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.ExpenseDocumentResponse;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.services.ExpenseDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expense-documents")
public class ExpenseDocumentController {

    private final ExpenseDocumentService expenseDocumentService;

    public ExpenseDocumentController(ExpenseDocumentService expenseDocumentService) {
        this.expenseDocumentService = expenseDocumentService;
    }

    //@PreAuthorize("hasAnyRole('Employee','HR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createExpenseDocument(@Valid @RequestPart("data") CreateExpenseDocumentRequest request,

                                                  @RequestPart("file") MultipartFile file){


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
            return ResponseEntity.ok(expenseDocumentService.creatExpenseDocument(request,file));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //@PreAuthorize("hasAnyRole('Employee','HR')")
//    @GetMapping("/{id}")
//    public ResponseEntity<List<ExpenseDocumentResponse>> getAllExpenseDocumentsByExpenseID(@PathVariable Long id){
//        try{
//            return ResponseEntity.ok(expenseDocumentService.getAllTravelDocuments());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
//    }
//
//
//
//    //@PreAuthorize("hasAnyRole('Employee','HR')")
//    @GetMapping("/{id}")
//    public ResponseEntity<ExpenseDocumentResponse> getExpenseDocumentById(@PathVariable long id){
//        try{
//            return ResponseEntity.ok(ExpenseDocumentService.getTravelDocumentById(id));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
//    }
}
