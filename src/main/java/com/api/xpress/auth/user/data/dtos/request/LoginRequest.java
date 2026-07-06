package com.api.xpress.auth.user.data.dtos.request;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record LoginRequest(
        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String password) {
}
