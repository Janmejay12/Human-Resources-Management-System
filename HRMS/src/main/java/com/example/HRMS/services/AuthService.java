package com.example.HRMS.services;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.RefreshToken;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;


    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JWTService jwtService, RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse authenticateUser(LoginRequest request, HttpServletResponse response) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            String accessToken = jwtService.generateToken(employee.getEmail(), employee.getRole().getRoleName().toString());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(employee);

            refreshTokenService.attachRefreshCookie(response, refreshToken.getToken());

            return new LoginResponse(accessToken);
        } else
            throw new RuntimeException("Invalid Password");
    }
    public void logout(HttpServletRequest request,
                       HttpServletResponse response){

        String token = refreshTokenService.extractTokenFromCookie(request);

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

        refreshTokenService.clearRefreshCookie(response);
    }


}
