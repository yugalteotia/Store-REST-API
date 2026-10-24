package com.yugal.store.auth;

import com.yugal.store.users.User;
import com.yugal.store.users.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponse login(@Valid AuthRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user).toString();
        var refreshToken = jwtService.generateRefreshToken(user).toString();

        return new LoginResponse(accessToken, refreshToken);
    }

    public String getFreshAccessToken(String refreshToken) {

        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired())
            throw new BadCredentialsException("Refresh token is invalid or expired");

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();

        return jwtService.generateAccessToken(user).toString();
    }
}
