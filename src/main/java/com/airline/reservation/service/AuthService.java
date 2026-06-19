package com.airline.reservation.service;

import com.airline.reservation.dto.AuthResponse;
import com.airline.reservation.dto.LoginRequest;
import com.airline.reservation.dto.RegisterRequest;
import com.airline.reservation.entity.Role;
import com.airline.reservation.entity.User;
import com.airline.reservation.repository.UserRepository;
import com.airline.reservation.security.JwtUtils;
import com.airline.reservation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void register(RegisterRequest request) {
        // check if email is already in database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        // create new user object using builder
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                // don't forget to encode the password!
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER) // default role is user
                .build();

        // save the user
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // authenticate the user with email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // generate jwt token
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // get the user role
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority())
                .orElse("ROLE_USER");

        // return the response with token
        return new AuthResponse(jwt, userDetails.getId(), userDetails.getUsername(), role);
    }
}
