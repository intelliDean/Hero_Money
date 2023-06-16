package com.loan.hero.loan_officer.data.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficerRequest {

    private String token;

    private String email;

    private String employeeId;

    private String password;

    private String phoneNumber;

    private MultipartFile userImage;

    private String houseNumber;

    private String streetName;

    private String city;

    private String state;

    private String zipCode;
}
