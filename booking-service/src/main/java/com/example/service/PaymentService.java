package com.example.service;

import com.example.domain.entity.Payment;
import com.example.domain.request.PaymentReqDTO;
import com.example.domain.request.SepayWebhookReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.domain.response.PaymentResDTO;
import com.example.util.error.IdInvalidException;

public interface PaymentService extends BaseService<Payment, Long, PaymentReqDTO, PaymentResDTO> {
    PaymentResDTO confirmCodPayment(Payment dto);
    OrderResDTO confirmSepayPayment(SepayWebhookReqDTO payload) throws IdInvalidException;
}
