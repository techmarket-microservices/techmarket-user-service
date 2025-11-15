package com.techmarket.user_service.controller;

import com.techmarket.user_service.constants.ApiPaths;
import com.techmarket.user_service.dto.request.AuthRequest;
import com.techmarket.user_service.dto.response.AuthResponse;
import com.techmarket.user_service.security.JwtService;
import com.techmarket.user_service.security.RefreshTokenService;
import com.techmarket.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping
    public String home(){
        return "hello zahaira";
    }

    @PostMapping(ApiPaths.REGISTER_URL)
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) throws Exception{
        return ResponseEntity.ok(userService.register(authRequest));
    }

    @PostMapping(ApiPaths.LOGIN_URL)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws Exception{
        return ResponseEntity.ok(userService.login(authRequest));
    }

    @PostMapping(ApiPaths.REFRESH_URL)
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refreshToken");

        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    if(refreshTokenService.isTokenExpired(token)){
                        refreshTokenService.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtService.generateToken(token.getUser());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }

    @PostMapping(ApiPaths.LOGOUT_URL)
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }
        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    refreshTokenService.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }
}
