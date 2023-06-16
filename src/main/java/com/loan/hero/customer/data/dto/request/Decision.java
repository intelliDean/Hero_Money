package com.loan.hero.customer.data.dto.request;

import com.loan.hero.customer.data.dto.response.AgreementDecision;
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
public class Decision {

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private Long loanAgreementId;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private AgreementDecision agreementDecision;
}
