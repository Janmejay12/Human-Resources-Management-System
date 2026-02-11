package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {

        this.expenseService = expenseService;
    }

    @PostMapping()
    public ResponseEntity<?> createExpense(@Valid @RequestBody CreateExpenseRequest request){
        try{
            return ResponseEntity.ok(expenseService.createExpense(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/by-travel/{id}")
    public ResponseEntity<List<ExpenseResponse>> getAllExpensesByTravelId(@PathVariable Long id){
        try{
            return ResponseEntity.ok(expenseService.getAllExpensesByTravelId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    @GetMapping("/by-employee/{id}")
    public ResponseEntity<List<ExpenseResponse>> getAllExpensesByEmployeeId(@PathVariable Long id){
        try{
            return ResponseEntity.ok(expenseService.getAllExpensesByEmployeeId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

   // @PreAuthorize("hasRole('HR')")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable long id){
        try{
            return ResponseEntity.ok(expenseService.getExpenseById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

}
