package com.loan.hero.customer.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotNull(message = "Token cannot be null")
    @NotBlank(message = "Token cannot be blank")
    private String token;

    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(max = 20, min = 8)
    private String password;
}
