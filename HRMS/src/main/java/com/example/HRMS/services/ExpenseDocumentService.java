package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.CreateExpenseDocumentRequest;
import com.example.HRMS.dtos.response.ExpenseDocumentResponse;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.ExpenseDocument;
import com.example.HRMS.mappers.ExpenseMapper;
import com.example.HRMS.repos.EmployeeRepository;
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
import java.util.stream.Collectors;

@Service
public class ExpenseDocumentService {
    private final ExpenseDocumentRepository expenseDocumentRepository;
    private final ExpenseRepository expenseRepository;
    private final Cloudinary cloudinary;
    private final EmployeeRepository employeeRepository;

    public ExpenseDocumentService(ExpenseDocumentRepository expenseDocumentRepository,EmployeeRepository employeeRepository, Cloudinary cloudinary, ExpenseRepository expenseRepository) {
        this.expenseDocumentRepository = expenseDocumentRepository;
        this.expenseRepository = expenseRepository;
        this.cloudinary = cloudinary;
        this.employeeRepository = employeeRepository;
    }

    public ExpenseDocumentResponse creatExpenseDocument(Long expenseId, MultipartFile file, String email)
    {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));

        boolean validEmployee = expense.getEmployee().getEmployeeId().equals(employee.getEmployeeId());

        if(!validEmployee)
            throw new IllegalArgumentException("This expense record doesnt belong to you.");

        ExpenseDocument expenseDocument = new ExpenseDocument();


        expenseDocument.setExpense(expense);

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
        expenseDocumentResponse.setExpenseId(expense.getExpenseId());
        expenseDocumentResponse.setStorageUrl(expenseDocument.getStorageUrl());

        expenseDocumentRepository.save(expenseDocument);
        return expenseDocumentResponse;
    }

    public List<ExpenseDocumentResponse> getAllExpenseDocumentsByExpenseID(Long expenseId)
    {
        List<ExpenseDocument> expenseDocuments = expenseDocumentRepository.findAllByExpenseId(expenseId);

        List<ExpenseDocumentResponse> expenseDocumentResponses = expenseDocuments.stream()
                .map(entity -> {
                    ExpenseDocumentResponse dto = new ExpenseDocumentResponse();
                    dto.setExpenseId(entity.getExpense().getExpenseId());
                    dto.setStorageUrl(entity.getStorageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
       return expenseDocumentResponses;
    }

    public ExpenseDocumentResponse getExpenseDocumentById(Long id)
    {
       ExpenseDocument expenseDocument = expenseDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense Document not found with ID: " + id));
       ExpenseDocumentResponse res = new ExpenseDocumentResponse();
       res.setExpenseId(expenseDocument.getExpense().getExpenseId());
       res.setStorageUrl(expenseDocument.getStorageUrl());
       return res;
    }
}
