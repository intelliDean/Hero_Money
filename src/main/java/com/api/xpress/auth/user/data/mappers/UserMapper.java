package com.api.xpress.auth.user.data.mappers;

import com.api.xpress.auth.user.data.dtos.UserDTO;
import com.api.xpress.auth.user.data.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

}
