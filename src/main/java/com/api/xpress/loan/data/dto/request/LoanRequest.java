package com.api.xpress.loan.data.dto.request;

import com.api.xpress.loan.data.models.PaymentFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

@Builder
public record LoanRequest (

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    String loanPurpose,

     @NotNull(message = NOT_NULL)
     @Positive(message = "Loan amount must be greater than zero")
    BigDecimal loanAmount,

    @NotNull(message = NOT_NULL)
    @Positive(message = "Repayment term must be greater than zero")
    Integer repaymentTerm,

    @NotNull(message = NOT_NULL)
    PaymentFrequency paymentFrequency,

    @NotNull
    MultipartFile paySlip,

    @NotNull(message = NOT_NULL)
    MultipartFile accountStatement
){}