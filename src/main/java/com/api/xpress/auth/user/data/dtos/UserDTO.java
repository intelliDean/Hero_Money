package com.api.xpress.auth.user.data.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.api.xpress.auth.user.data.models.Address;
import com.api.xpress.auth.user.data.models.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserDTO(

        String firstName,

        String lastName,

        String email,

        @JsonUnwrapped
        Address address,

        Set<Role> roles,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, d MMMM, yyyy hh:mm:ssa")
        LocalDateTime registeredAt,

        boolean enabled
) {
}
