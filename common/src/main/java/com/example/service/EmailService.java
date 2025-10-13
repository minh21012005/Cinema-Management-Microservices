package com.example.service;

import com.example.domain.entity.TicketEmailDTO;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
    void sendTicketEmail(String toEmail, TicketEmailDTO ticketInfo);
}
