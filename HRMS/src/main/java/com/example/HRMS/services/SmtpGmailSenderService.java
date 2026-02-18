package com.example.HRMS.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SmtpGmailSenderService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String fromEmail,String toEmail, String subject, String bodyText, String linkUrl, String linkText){
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Constructing the HTML content with the embedded link
            String htmlBody = bodyText + "<p>Please view the document here: <a href=\"" + linkUrl + "\">" + linkText + "</a></p>";

            helper.setText(htmlBody, true);

            emailSender.send(message);
            System.out.println("Email sent successfully with link");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }

    }

    public void sendEmail(String fromEmail, String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);

        System.out.println("Message sent successfully");
    }

}
