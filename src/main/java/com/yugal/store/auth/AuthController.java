package com.yugal.store.auth;

import com.yugal.store.users.UserDto;
import com.yugal.store.users.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import static java.time.Duration.ofSeconds;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login, token refresh, and the current user")
public class AuthController {

    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Authenticates a user, returns an access token, and sets the refresh-token cookie.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or request")
    })
    @SecurityRequirements
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody AuthRequestDto request, HttpServletResponse response) {
        var loginResponse = authService.login(request);

        // SameSite=None; Secure is required for cross-site hosted use:
        // frontend is ${FRONTEND_WEBSITE_URL}, for now
        // backend is https://store-rest-api.hostless.app. Without it, browsers withhold
        // this cookie on POST /auth/refresh and sessions do not restore
        // after reload / access-token expiry.
        var cookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(ofSeconds(jwtConfig.getRefreshTokenExpiration())) // 7 days
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

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
    @Operation(summary = "Refresh the access token", description = "Creates a new access token from the refresh-token cookie.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid refresh token")
    })
    @SecurityRequirements
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.getFreshAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current user",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Current user was not found")
    })
    @SecurityRequirement(name = "bearerAuth")
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
