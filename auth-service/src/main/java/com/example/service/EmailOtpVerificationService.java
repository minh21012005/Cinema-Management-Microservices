package com.example.service;

import com.example.domain.request.CreateUserRequest;
import com.example.util.error.IdInvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface EmailOtpVerificationService {
    ResponseEntity<?> sendOtp(CreateUserRequest req) throws IdInvalidException, JsonProcessingException;
}
