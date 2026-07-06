package com.api.xpress.loan_officer.data.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

@Builder
public record AgreementRequest(

        @NotNull(message = NOT_NULL)
        Long loanId,

        @NotNull(message = NOT_NULL)
        @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
        BigDecimal interestRate
) {
}
