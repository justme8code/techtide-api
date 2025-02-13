package com.justme8code.techtide.controllers;

import com.justme8code.techtide.custom_responses.LoginResponse;
import com.justme8code.techtide.models.UserLogin;
import com.justme8code.techtide.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> requestLogin(
            @RequestPart("username") String username,
            @RequestPart("password") String password,
            HttpServletResponse response,
            HttpServletRequest request) {

        UserLogin userLogin = new UserLogin(username, password);
        return ResponseEntity.ok(authService.login(userLogin, response, request));
    }

    @DeleteMapping()
    public ResponseEntity<Void> requestLogout(HttpServletResponse response){
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}
