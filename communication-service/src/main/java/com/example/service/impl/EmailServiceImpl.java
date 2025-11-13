package com.example.service.impl;

import com.example.domain.entity.TicketEmailDTO;
import com.example.service.EmailService;
import com.example.util.EncryptionUtil;
import com.example.util.QrCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${encryption.secret}")
    private String secret;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender,
                            SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            // ✅ 1. Tạo dữ liệu truyền vào template
            Context context = new Context();
            context.setVariable("otp", otp);

            // ✅ 2. Render file HTML template
            String htmlContent = templateEngine.process("email/otp-email.html", context);

            // ✅ 3. Tạo và gửi email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("🔐 Xác thực đăng ký tài khoản CNM");
            helper.setText(htmlContent, true); // HTML email
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email OTP", e);
        }
    }

    @Override
    public void sendTicketEmail(TicketEmailDTO ticketInfo) {
        try {
            // Tạo giá trị QR
            String qrCodeValue = EncryptionUtil.encrypt(String.valueOf(ticketInfo.getOrderId()), secret);

            // Tạo QR dưới dạng byte array
            byte[] qrBytes = QrCodeUtil.generateQrCodeBytes(qrCodeValue, 200, 200);

            // Chuẩn bị Thymeleaf context
            Context context = new Context();
            context.setVariable("ticket", ticketInfo);

            // HTML template
            String htmlContent = templateEngine.process("email/ticket-confirmation.html", context);

            // Tạo email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true = multipart
            helper.setTo(ticketInfo.getEmail());
            helper.setSubject("🎟️ Vé xem phim của bạn tại CNM đã được xác nhận!");
            helper.setText(htmlContent, true);

            // Thêm QR dưới dạng inline attachment
            helper.addInline("ticketQr", new ByteArrayResource(qrBytes), "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email vé", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
