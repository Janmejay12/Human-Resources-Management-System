package com.example.HRMS.services;

import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.RefreshToken;
import com.example.HRMS.repos.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.cloudinary.AccessControlRule.AccessType.token;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;


    @Value("${jwt.refreshExpiration}")
    private Long refreshTokenDuration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JWTService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public RefreshToken createRefreshToken(Employee user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        return refreshTokenRepository.save(refreshToken);
    }

    public LoginResponse refreshToken(HttpServletRequest request,
                                      HttpServletResponse response) {

        String token = extractTokenFromCookie(request);

        RefreshToken oldToken = verifyToken(token);

        RefreshToken newToken =
                rotateRefreshToken(oldToken);

        attachRefreshCookie(response, newToken.getToken());

        String accessToken = jwtService.generateToken(
                newToken.getUser().getEmail(),
                newToken.getUser().getRole().getRoleName().toString()
        );

        return new LoginResponse(accessToken);

    }

    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            revokeAll(refreshToken.getUser());
            throw new RuntimeException("Refresh token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return createRefreshToken(oldToken.getUser());
    }

    public void revokeAll(Employee user) {

        List<RefreshToken> tokens =
                refreshTokenRepository.findByUser(user);

        tokens.forEach(t -> t.setRevoked(true));

        refreshTokenRepository.saveAll(tokens);
    }
    public void attachRefreshCookie(HttpServletResponse response, String token) {

        Cookie cookie = new Cookie("refreshToken", token);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/refresh");
        cookie.setPath("/api/auth/logout");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);
    }
    public String extractTokenFromCookie(HttpServletRequest request) {

        if (request.getCookies() == null) {
            throw new RuntimeException("Refresh token missing");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }
    public void clearRefreshCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/refresh");
        cookie.setPath("/api/auth/logout");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

}
