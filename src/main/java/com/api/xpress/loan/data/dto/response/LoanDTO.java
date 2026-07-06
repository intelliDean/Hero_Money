package com.api.xpress.loan.data.dto.response;

import com.api.xpress.loan.data.models.LoanStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record LoanDTO(

        String message,

        LocalDateTime applicationDate,

        LoanStatus loanStatus
) {}