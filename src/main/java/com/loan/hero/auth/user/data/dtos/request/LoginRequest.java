package com.loan.hero.auth.user.data.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String email;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String password;

}
