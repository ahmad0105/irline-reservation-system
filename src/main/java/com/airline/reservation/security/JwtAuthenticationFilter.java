package com.airline.reservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // get the token from the request header
            String jwt = parseJwt(request);
            
            // if we have a token and it is valid
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // get username from token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // load the user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // create an authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                                
                // add details about the request                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // save the user to the security context so Spring knows they are logged in
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // log the error if something goes wrong
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // continue with the next filter
        filterChain.doFilter(request, response);
    }

    // helper method to extract token from header
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // token should start with 'Bearer '
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
