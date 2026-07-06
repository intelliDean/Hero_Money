package com.api.xpress.customer.data.dto.response;

import com.api.xpress.auth.user.data.dtos.UserDTO;
import com.api.xpress.customer.data.models.enums.Gender;
import com.api.xpress.customer.data.models.enums.JobStatus;
import com.api.xpress.customer.data.models.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerDTO(

        UserDTO user,

        int age,
        MaritalStatus maritalStatus,

        JobStatus jobStatus,

        BigDecimal salary,

        String companyName,

        Gender gender,

        String paySlip,

        String formOfIdentity,

        String accountStatement
) {
}
