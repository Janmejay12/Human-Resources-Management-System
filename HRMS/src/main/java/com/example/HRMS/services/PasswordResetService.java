package com.example.HRMS.services;

import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.PasswordResetToken;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.PasswordResetTokenRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final EmployeeRepository employeeRepository;
    private final SmtpGmailSenderService smtpGmailSenderService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepo passwordResetTokenRepo,PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, SmtpGmailSenderService smtpGmailSenderService) {
        this.passwordResetTokenRepo = passwordResetTokenRepo;
        this.employeeRepository = employeeRepository;
        this.smtpGmailSenderService = smtpGmailSenderService;
        this.passwordEncoder = passwordEncoder;

    }

    public void forgotPassword(String email){
        Employee user = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found with email: " + email)
                );

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        passwordResetTokenRepo.save(resetToken);

        smtpGmailSenderService.sendEmail("janmejay.raj121@gmail.com", email,
                "Reset Password Link",
                "Use this link to reset password : http://localhost:5173/reset-password?token=" + token);
    }

    public void resetPassword(String token, String password){
        PasswordResetToken resetToken =
                passwordResetTokenRepo.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        Employee user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(password));

        employeeRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepo.save(resetToken);
    }


}
