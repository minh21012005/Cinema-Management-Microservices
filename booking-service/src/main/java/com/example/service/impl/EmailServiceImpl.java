package com.example.service.impl;

import com.example.domain.entity.TicketEmailDTO;
import com.example.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender,
                            SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
    }

    @Async
    @Override
    public void sendTicketEmail(String toEmail, TicketEmailDTO ticketInfo) {
        try {
            Context context = new Context();
            context.setVariable("ticket", ticketInfo);

            String htmlContent = templateEngine.process("email/ticket-confirmation.html", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("🎟️ Vé xem phim của bạn tại CNM đã được xác nhận!");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email vé", e);
        }
    }

}
