package com.loan.hero.loan.data.dto.request;

import com.loan.hero.loan.data.models.PaymentFrequency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    private String loanPurpose;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private BigDecimal loanAMount;


    private int repaymentTerm;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private PaymentFrequency paymentFrequency;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private MultipartFile paySlip;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private MultipartFile accountStatement;
}