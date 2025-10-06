package com.example.service.impl;

import com.example.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "Xác thực đăng ký tài khoản";
        String body = """
                Xin chào,
                
                Đây là mã OTP để xác thực đăng ký tài khoản của bạn: %s
                
                Mã OTP này sẽ hết hạn sau 5 phút.
                
                Trân trọng,
                Đội ngũ hỗ trợ Dreamers Entertainment
                """.formatted(otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
