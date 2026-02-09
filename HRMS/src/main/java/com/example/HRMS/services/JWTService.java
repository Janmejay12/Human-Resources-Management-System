package com.example.HRMS.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
    @Value("${jwt.secretKey}")
    private String secretkey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;
}
