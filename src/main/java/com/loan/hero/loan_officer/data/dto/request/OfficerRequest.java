package com.loan.hero.loan_officer.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static com.loan.hero.hero_utility.HeroUtilities.NOT_BLANK;
import static com.loan.hero.hero_utility.HeroUtilities.NOT_NULL;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficerRequest {

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String token;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String email;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String employeeId;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String password;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String phoneNumber;

    @NotNull(message = NOT_NULL)
    private MultipartFile userImage;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String houseNumber;

    @NotNull(message = NOT_NULL)
    @NotBlank(message = NOT_BLANK)
    private String streetName;

    private String city;

    private String state;

    private String zipCode;
}
