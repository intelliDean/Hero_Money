package com.loan.hero.loan.data.dto.request;

import com.loan.hero.loan.data.models.PaymentFrequency;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    private String loanPurpose;

    private BigDecimal loanAMount;

    private PaymentFrequency paymentFrequency;

    private MultipartFile paySlip;

    private MultipartFile accountStatement;
}