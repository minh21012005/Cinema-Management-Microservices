package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TransactionResDTO {
    private Long id;
    private String staffName;
    private String customerName;
    private Double amount;
    private String method;
    private String status;
    private String date;
}
