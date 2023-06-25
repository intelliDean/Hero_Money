package com.loan.hero.customer.data.dto.request;

import com.loan.hero.customer.data.models.enums.AgreementDecision;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Decision {

    @NotNull(message = NOT_NULL)
    private Long loanAgreementId;

    @NotNull(message = NOT_NULL)
    private AgreementDecision agreementDecision;
}
