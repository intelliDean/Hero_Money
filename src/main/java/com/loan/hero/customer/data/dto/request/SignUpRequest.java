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


    private String token;


    private String firstName;


    private String lastName;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(max = 20, min = 8)
    private String password;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String dateOfBirth;
}
