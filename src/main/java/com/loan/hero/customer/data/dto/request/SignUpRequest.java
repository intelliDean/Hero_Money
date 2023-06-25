package com.loan.hero.customer.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String token;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String firstName;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String lastName;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String email;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    @Size(max = 20, min = 8)
    private String password;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String dateOfBirth;
}
