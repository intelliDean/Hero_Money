package com.api.xpress.loan_officer.data.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.api.xpress.auth.user.data.models.Address;
import com.api.xpress.auth.user.data.models.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Builder
public record LoanOfficerDTO(

        String firstName,

        String lastName,

        String email,

        @JsonUnwrapped
        Address address,

        String userImage,

        String phoneNumber,

        Set<Role> roles,

        LocalDateTime registeredAt,

        boolean enabled
) {
}
