package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.RegisterRequest;
import com.example.HRMS.dtos.response.EmployeeResponse;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.dtos.response.RegisterResponse;
import com.example.HRMS.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/register-employee")
    public ResponseEntity<RegisterResponse> registerEmployee(@Valid @RequestBody RegisterRequest request){
        try{
            return ResponseEntity.ok(adminService.registerEmployee(request));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new RegisterResponse("Registering Employee Failed: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('Admin','HR')")
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
        try{
            return ResponseEntity.ok(adminService.getAllEmployees());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

}
