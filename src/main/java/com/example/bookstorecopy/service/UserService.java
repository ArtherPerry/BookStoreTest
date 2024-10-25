package com.example.bookstorecopy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(String username, String newPassword) {
        // Fetch the existing user
        InMemoryUserDetailsManager userDetailsManager = (InMemoryUserDetailsManager) userDetailsService;

        // Load the existing user
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // Create new user details with the updated password
        UserDetails updatedUser = User.builder()
                .username(userDetails.getUsername())
                .password(passwordEncoder.encode(newPassword))
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .build();

        // Update the user in the manager
        userDetailsManager.updateUser(updatedUser);
    }

    public void createUser(String username, String password, String... roles) {
        InMemoryUserDetailsManager userDetailsManager = (InMemoryUserDetailsManager) userDetailsService;

        // Create new user details
        UserDetails newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        // Add new user to the manager
        userDetailsManager.createUser(newUser);
    }

    public void resetPassword(String username, String defaultPassword) {
        changePassword(username, defaultPassword);
    }
}
