package com.loan.hero.loan_officer.data.dto.request;

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
public class InviteRequest {

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String firstName;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String lastName;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String email;
}