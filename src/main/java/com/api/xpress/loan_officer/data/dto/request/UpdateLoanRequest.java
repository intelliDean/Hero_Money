package com.api.xpress.loan_officer.data.dto.request;

import com.api.xpress.loan.data.models.LoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

@Builder
public record UpdateLoanRequest(

        @NotNull(message = NOT_NULL)
        Long loanId,

        @NotNull(message = NOT_NULL)
        LoanStatus loanStatus
) {
}
