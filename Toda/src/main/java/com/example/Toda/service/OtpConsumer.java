package com.example.Toda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OtpConsumer {
    @Autowired
    private JavaMailSender mailSender;

    @JmsListener(destination = "otp-queue")
    public void consumeOtpMessage(Map<String, String> messageData) {
        String email = messageData.get("email");
        String otp = messageData.get("otp");


        System.out.println("Consumer received from Artemis: Sending OTP " + otp + " to " + email);

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("amrn38420@gmail.com");
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Hello, your OTP code is: " + otp);

            mailSender.send(message);

            System.out.println(">>> Email sent successfully to " + email);

        } catch (Exception e) {
            System.err.println(">>> Failed to send email: " + e.getMessage());
        }
    }
}
