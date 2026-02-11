package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.CreateExpenseDocumentRequest;
import com.example.HRMS.dtos.response.ExpenseDocumentResponse;
import com.example.HRMS.entities.ExpenseDocument;
import com.example.HRMS.repos.ExpenseDocumentRepository;
import com.example.HRMS.repos.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExpenseDocumentService {
    private final ExpenseDocumentRepository expenseDocumentRepository;
    private final ExpenseRepository expenseRepository;
    private final Cloudinary cloudinary;

    public ExpenseDocumentService(ExpenseDocumentRepository expenseDocumentRepository,Cloudinary cloudinary, ExpenseRepository expenseRepository) {
        this.expenseDocumentRepository = expenseDocumentRepository;
        this.expenseRepository = expenseRepository;
        this.cloudinary = cloudinary;
    }

    public ExpenseDocumentResponse creatExpenseDocument(CreateExpenseDocumentRequest request, MultipartFile file){
        ExpenseDocument expenseDocument = new ExpenseDocument();

        expenseDocument.setExpense(expenseRepository.findById(request.getExpenseId())
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + request.getExpenseId())));

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()+"_"+expenseDocument.getExpenseDocumentId());
        try {

            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            var doc = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/expenseDocuments/"));

            expenseDocument.setStorageUrl(doc.get("url").toString());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload the file.");
        }

        ExpenseDocumentResponse expenseDocumentResponse = new ExpenseDocumentResponse();
        expenseDocumentResponse.setExpenseId(request.getExpenseId());
        expenseDocumentResponse.setStorageUrl(expenseDocument.getStorageUrl());

        return expenseDocumentResponse;
    }

    public List<ExpenseDocument> getAllExpenseDocumentsByExpenseID(Long expenseId){
        
    }
}
