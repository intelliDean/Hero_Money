package com.api.xpress.customer.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;

@Builder
public record InitRequest(

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Email(message = "Invalid email format")
        String email
) {}
