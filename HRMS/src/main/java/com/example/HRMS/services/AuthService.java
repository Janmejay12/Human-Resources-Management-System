package com.example.HRMS.services;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.repos.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private JWTService jwtService;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse authenticateUser(LoginRequest request){
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if(passwordEncoder.matches(request.getPassword(),employee.getPassword())){
            String token = jwtService.generateToken(employee.getEmail(), employee.getRole().getRoleName().toString());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            return loginResponse;
        }
        else
            throw new RuntimeException("Invalid Password");
    }

}
