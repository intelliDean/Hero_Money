package com.loan.hero.customer.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loan.hero.customer.data.models.enums.Gender;
import com.loan.hero.customer.data.models.enums.JobStatus;
import com.loan.hero.customer.data.models.enums.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCustomerRequest {

    @NotNull(message = NOT_NULL)
    private MaritalStatus maritalStatus;

    @NotNull(message = NOT_NULL)
    private JobStatus jobStatus;

    @NotNull(message = NOT_NULL)
    private BigDecimal salary;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String companyName;

    @NotNull(message = NOT_NULL)
    private Gender gender;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String houseNumber;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String streetName;

    private String city;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String state;

    private String zipCode;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String phoneNumber;

    @NotNull(message = NOT_NULL)
    private MultipartFile userImage;

    @NotNull(message = NOT_NULL)
    private MultipartFile formOfIdentity;
}
