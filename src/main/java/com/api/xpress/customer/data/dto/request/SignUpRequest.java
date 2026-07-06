package com.api.xpress.customer.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;
import static com.api.xpress.xpress_utils.XpressUtils.VALID_PASSWORD;

@Builder
public record SignUpRequest(

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String token,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String firstName,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String lastName,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Pattern(regexp = VALID_PASSWORD, message = "Password must be at least 8 characters long, contain at least one letter, one number, and one special character")
        @Size(max = 20)
        String password,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "Date of birth must be in the format dd/MM/yyyy")
        String dateOfBirth
) {
}
