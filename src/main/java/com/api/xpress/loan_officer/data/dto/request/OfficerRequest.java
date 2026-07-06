package com.api.xpress.loan_officer.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;
import static com.api.xpress.xpress_utils.XpressUtils.VALID_PASSWORD;
import static com.api.xpress.xpress_utils.XpressUtils.VALID_NUMBER;

@Builder
public record OfficerRequest(

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String token,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String employeeId,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Pattern(regexp = VALID_PASSWORD, message = "Password must be at least 8 characters long, contain at least one letter, one number, and one special character")
        @Size(max = 20)
        String password,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Pattern(regexp = VALID_NUMBER, message = "Invalid phone number format")
        String phoneNumber,

        @NotNull(message = NOT_NULL)
        MultipartFile userImage,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String houseNumber,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String streetName,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String city,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String state,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String zipCode
) {
}
