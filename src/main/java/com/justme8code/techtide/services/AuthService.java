package com.justme8code.techtide.services;

import com.justme8code.techtide.custom_responses.LoginResponse;
import com.justme8code.techtide.models.User;
import com.justme8code.techtide.models.UserLogin;
import com.justme8code.techtide.util.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void addUser(User user, UserRole userRole);
    LoginResponse login(UserLogin userLogin, HttpServletResponse response, HttpServletRequest request);
    void logout( HttpServletResponse response);
    void createNewUser(User user);
}
