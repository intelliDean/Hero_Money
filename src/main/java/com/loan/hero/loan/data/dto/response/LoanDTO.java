package com.loan.hero.loan.data.dto.response;

import com.loan.hero.loan.data.models.LoanStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private String message;

    private LocalDateTime applicationDate;

    private LoanStatus loanStatus;
}
