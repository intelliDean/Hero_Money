package com.loan.hero.customer.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loan.hero.customer.data.models.Gender;
import com.loan.hero.customer.data.models.JobStatus;
import com.loan.hero.customer.data.models.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCustomerRequest {

    private MaritalStatus maritalStatus;

    private JobStatus jobStatus;

    private BigDecimal salary;

    private String companyName;

    private Gender gender;

    private String houseNumber;

    private String streetName;

    private String city;

    private String state;

    private String zipCode;

    private String phoneNumber;

    private MultipartFile userImage;

    private MultipartFile formOfIdentity;

   // private MultipartFile paySlip;

   // private MultipartFile accountStatement;
}
