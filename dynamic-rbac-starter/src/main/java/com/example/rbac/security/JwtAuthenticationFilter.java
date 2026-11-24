package com.example.rbac.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        System.out.println("========== JWT FILTER ==========");
        System.out.println("Request: " + request.getRequestURI());
        System.out.println("Authorization header present: " + (header != null));

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String username = jwtService.extractUsername(token);
                System.out.println("JWT subject: " + username);

                UserDetails user =
                        userDetailsService.loadUserByUsername(username);

                boolean valid = jwtService.isValid(token, user);
                System.out.println("JWT valid: " + valid);

                if (valid) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    SecurityContextHolder.getContext()
                            .setAuthentication(auth);

                    System.out.println("Authentication established: "
                            + SecurityContextHolder.getContext()
                            .getAuthentication().isAuthenticated());
                }

            } catch (Exception e) {
                System.out.println("JWT ERROR: " + e.getClass().getName()
                        + " : " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

}