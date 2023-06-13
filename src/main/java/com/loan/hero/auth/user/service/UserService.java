package com.loan.hero.auth.user.service;

import com.loan.hero.auth.user.data.dtos.UserDTO;
import com.loan.hero.auth.user.data.models.User;
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

    UserDTO currentUser();

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
