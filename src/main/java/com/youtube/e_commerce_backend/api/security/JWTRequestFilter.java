package com.youtube.e_commerce_backend.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import com.youtube.e_commerce_backend.services.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JwtTokenService JwtTokenService;
    private LocalUserDAO localUserDAO;

    public JWTRequestFilter(JwtTokenService JWTTokenService,
                            LocalUserDAO localUserDAO) {
        this.localUserDAO = localUserDAO;
        this.JwtTokenService = JWTTokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = JwtTokenService.getUsername(token);
                Optional<LocalUser> optUser = localUserDAO.findByUsernameIgnoreCase(username);
                if (optUser.isPresent()) {
                    LocalUser user = optUser.get();
                    if(user.getIsEmailVerified()) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            catch (JWTDecodeException ignored) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
