package com.api.xpress.customer.data.dto.request;

import com.api.xpress.customer.data.models.enums.AgreementDecision;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;


@Builder
public record Decision(

        @NotNull(message = NOT_NULL)
        Long loanAgreementId,

        @NotNull(message = NOT_NULL)
        AgreementDecision agreementDecision
) {}
