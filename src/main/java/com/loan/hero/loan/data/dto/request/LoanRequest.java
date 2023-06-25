package com.loan.hero.loan.data.dto.request;

import com.loan.hero.loan.data.models.PaymentFrequency;
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

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String loanPurpose;

     @NotNull(message = NOT_NULL)
    private BigDecimal loanAMount;

    @NotNull(message = NOT_NULL)
    private Integer repaymentTerm;

    @NotNull(message = NOT_NULL)
    private PaymentFrequency paymentFrequency;

    @NotNull
    private MultipartFile paySlip;

    @NotNull(message = NOT_NULL)
    private MultipartFile accountStatement;
}