package com.yugal.store.auth;

import com.yugal.store.users.UserDto;
import com.yugal.store.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody AuthRequestDto request, HttpServletResponse response) {
        var loginResponse = authService.login(request);

        var cookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(loginResponse.getAccessToken()));
    }

//    @PostMapping("/validate")
//    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
//
//        System.out.println("Validate called");
//
//        return jwtService.validateToken(authHeader.replace("Bearer ", ""));
//    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.getFreshAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    // Although I have added this but this is not required as spring security will handle this automatically and return 401 status code
    // even the email is not found in the database, spring security will not return 403.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(401).build();
    }
}
