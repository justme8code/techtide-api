package com.justme8code.techtide.services.impls;


import com.justme8code.techtide.models.User;
import com.justme8code.techtide.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String uniqueIdentifier) throws UsernameNotFoundException {
        // Find the user by id (assumed to be a string)
        User user = userRepository.findByUsernameEqualsIgnoreCaseOrIdEqualsIgnoreCase(uniqueIdentifier,uniqueIdentifier)
                .orElseThrow(() -> new AccessDeniedException("Wrong username or password"));
        // Convert roles to GrantedAuthority

        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getUserRole().name()))
                .toList();

        // Return a UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                authorities
        );
    }
}
