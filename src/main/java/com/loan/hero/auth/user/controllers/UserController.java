package com.loan.hero.auth.user.controllers;

import com.loan.hero.auth.user.data.dtos.UserDTO;
import com.loan.hero.auth.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Tag(name = "User Controller")
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;


    @GetMapping("current")
    @Operation(summary = "Get current user logged in")
    public ResponseEntity<UserDTO> currentUser() {
        return ResponseEntity.ok(
                userService.currentUser()
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.logout(request, response);
    }
    @GetMapping("refresh")
    @Operation(summary = "Get refresh token when access token expires")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);
    }
}