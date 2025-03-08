package com.justme8code.techtide.services.impls;

import com.justme8code.techtide.custom_responses.LoginResponse;
import com.justme8code.techtide.exceptions.UnExpectedException;
import com.justme8code.techtide.models.ResetPasswordRequest;
import com.justme8code.techtide.models.Role;
import com.justme8code.techtide.models.User;
import com.justme8code.techtide.models.UserLogin;
import com.justme8code.techtide.repositories.RoleRepository;
import com.justme8code.techtide.repositories.UserRepository;
import com.justme8code.techtide.services.interfaces.AuthService;
import com.justme8code.techtide.util.JwtAuthorizer;
import com.justme8code.techtide.util.RequestResponseUtil;
import com.justme8code.techtide.util.SecurityUtils;
import com.justme8code.techtide.util.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtAuthorizer authorizer;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtAuthorizer authorizer, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.authorizer = authorizer;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user, UserRole userRole) {
        // Assign default role (USER)
        assignRoleToUser(user,userRole);
        saveUser(user);
    }

    @Override
    public LoginResponse login(UserLogin userLogin, HttpServletResponse response, HttpServletRequest request) {
        User user = new User();
        user.setUsername(userLogin.getUsername());
        user.setPassword(userLogin.getPassword());
        Authentication authentication = SecurityUtils.authenticateUser(user,authenticationManager);
        String token = authorizer.generateToken(authentication);
        RequestResponseUtil.addAuthCookieToResponse(response,token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(SecurityUtils.getCurrentUserId());
        return loginResponse;
    }

    @Override
    public void logout(HttpServletResponse response) {
        RequestResponseUtil.removeAuthCookieFromResponse(response);
    }

    @Override
    public void createNewUser(User user) {
         userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = retreiveUser(resetPasswordRequest);
        if (user.getPassword() == null) {  // Only reset if password is NULL
            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Password reset successful");
        }
    }

    @Override
    public void changePassword(ResetPasswordRequest resetPasswordRequest) {
        User user = retreiveUser(resetPasswordRequest);

        if(user.getPassword() != null &&  passwordEncoder.matches(resetPasswordRequest.getPassword(), user.getPassword())){
             user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
             userRepository.save(user);
        }else{
            throw new UsernameNotFoundException("Password reset not successful");
        }
    }

    private User retreiveUser(ResetPasswordRequest resetPasswordRequest) {
        return userRepository.findByUsername(resetPasswordRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void assignRoleToUser(User user, UserRole userRole) {
        verifyAdminRole();

        Optional<User> targetUser = userRepository.findByUsername(user.getUsername());
        if (targetUser.isPresent()) {
            throw new UnExpectedException("User already exists");
        }
        Role role = roleRepository.findRoleByUserRole(userRole)
                .orElseThrow(() -> new UnExpectedException("Role not found"));
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);
    }

    private void saveUser(User user){
        // Assign the role to the user
        userRepository.save(user);
    }

    private void verifyAdminRole( ){
        // Get the currently logged-in user ID
        String currentUserId = SecurityUtils.getCurrentUserId();
        // Fetch the admin user
        User admin = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Ensure the logged-in user is an admin
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(role -> role.getUserRole().equals(UserRole.ROLE_ADMIN));
        if (!isAdmin) {
            throw new UnExpectedException("Not authorized to assign roles");
        }
    }

}
