package com.loan.hero.loan_officer.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementRequest {

    @NotNull(message = NOT_NULL)
    private Long loanId;

    @NotNull(message = NOT_NULL)
    private BigDecimal interestRate;
}
