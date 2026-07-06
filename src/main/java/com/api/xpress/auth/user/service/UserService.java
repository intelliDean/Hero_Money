package com.api.xpress.auth.user.service;

import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.user.data.dtos.UserDTO;
import com.api.xpress.auth.user.data.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {
    User findUserByEmail(String email);


    void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    User getCurrentUser();

    UserDTO currentUser(AuthenticatedUser currentUser);

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
