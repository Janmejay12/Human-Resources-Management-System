package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.*;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {

        this.expenseService = expenseService;
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<?> createExpense(@AuthenticationPrincipal CustomEmployee user, @Valid @RequestBody CreateExpenseRequest request, @PathVariable Long id){
        try{
            return ResponseEntity.ok(expenseService.createExpense(request,user.getUsername(),id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<List<ExpenseResponse>> getAllExpensesByTravelId(@PathVariable Long id){
        try{
            return ResponseEntity.ok(expenseService.getAllExpensesByTravelId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    @PostMapping("/{travelId}/expenses/my-expenses")
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(@AuthenticationPrincipal CustomEmployee user,@PathVariable Long travelId){
        try{
            return ResponseEntity.ok(expenseService.getMyExpenses(travelId,user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

   // @PreAuthorize("hasRole('HR')")
    @GetMapping("/{travelId}/expenses/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable long travelId,@PathVariable long id){
        try{
            return ResponseEntity.ok(expenseService.getExpenseById(travelId,id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    @PutMapping("/{travelId}/expenses/{expenseId}/status")
    public ResponseEntity<?> changeExpenseStatus(@AuthenticationPrincipal CustomEmployee user
            ,@RequestBody ChangeExpenseStatusRequest request
            ,@PathVariable Long travelId ,@PathVariable Long expenseId)
    {
        try{
            return ResponseEntity.ok(expenseService.changeExpenseStatus(request, user.getUsername(), travelId, expenseId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
