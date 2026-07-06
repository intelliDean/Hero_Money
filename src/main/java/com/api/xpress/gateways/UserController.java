package com.api.xpress.gateways;

import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.user.CurrentUser;
import com.api.xpress.auth.user.data.dtos.request.LoginRequest;
import com.api.xpress.auth.user.data.dtos.UserDTO;
import com.api.xpress.auth.user.service.UserService;
import com.api.xpress.exceptions.XpressException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller")
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;


    @GetMapping("/current")
    @Operation(summary = "Get current user logged in")
    public ResponseEntity<UserDTO> currentUser(@CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(userService.currentUser(currentUser));
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public void login(@RequestBody @Valid LoginRequest request) {

        throw new XpressException("Authentication failed");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.logout(request, response);
    }

    @GetMapping("/refresh")
    @Operation(summary = "Get refresh token when access token expires")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.refreshToken(request, response);
    }
}
