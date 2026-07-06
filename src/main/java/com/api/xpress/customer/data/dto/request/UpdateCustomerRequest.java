package com.api.xpress.customer.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.api.xpress.customer.data.models.enums.Gender;
import com.api.xpress.customer.data.models.enums.JobStatus;
import com.api.xpress.customer.data.models.enums.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static com.api.xpress.xpress_utils.XpressUtils.NOT_BLANK;
import static com.api.xpress.xpress_utils.XpressUtils.NOT_NULL;
import static com.api.xpress.xpress_utils.XpressUtils.VALID_NUMBER;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateCustomerRequest(

        @NotNull(message = NOT_NULL)
        MaritalStatus maritalStatus,

        @NotNull(message = NOT_NULL)
        JobStatus jobStatus,

        @NotNull(message = NOT_NULL)
        @Positive(message = "Salary must be greater than zero")
        BigDecimal salary,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        String companyName,

        @NotNull(message = NOT_NULL)
        Gender gender,

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
        String zipCode,

        @NotNull(message = NOT_NULL)
        @NotBlank(message = NOT_BLANK)
        @Pattern(regexp = VALID_NUMBER, message = "Invalid phone number format")
        String phoneNumber,

        @NotNull(message = NOT_NULL)
        MultipartFile userImage,

        @NotNull(message = NOT_NULL)
        MultipartFile formOfIdentity
) {
}
