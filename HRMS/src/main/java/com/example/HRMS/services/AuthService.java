package com.example.HRMS.services;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.request.RefreshTokenRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.RefreshToken;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.RefreshTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;


    public AuthService(EmployeeRepository employeeRepository,RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse authenticateUser(LoginRequest request) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            String accessToken = jwtService.generateToken(employee.getEmail(), employee.getRole().getRoleName().toString());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(employee);

            return new LoginResponse(accessToken,refreshToken.getToken());
        } else
            throw new RuntimeException("Invalid Password");
    }


}
