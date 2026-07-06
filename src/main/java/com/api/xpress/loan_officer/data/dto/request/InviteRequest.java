package com.api.xpress.loan_officer.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

@Builder
public record InviteRequest(

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String firstName,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK) String lastName,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Email(message = "Invalid email format")
        String email
) {
}