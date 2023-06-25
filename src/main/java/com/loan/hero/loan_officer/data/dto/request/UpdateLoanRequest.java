package com.loan.hero.loan_officer.data.dto.request;

import com.loan.hero.loan.data.models.LoanStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLoanRequest {

    @NotNull(message = NOT_NULL)
    private Long loanId;

    @NotNull(message = NOT_NULL)
    private LoanStatus loanStatus;
}
