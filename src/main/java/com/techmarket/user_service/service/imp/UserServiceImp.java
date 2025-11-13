package com.techmarket.user_service.service.imp;

import com.techmarket.user_service.constants.MessageConstants;
import com.techmarket.user_service.dto.request.AuthRequest;
import com.techmarket.user_service.dto.response.AuthResponse;
import com.techmarket.user_service.exceptions.FunctionalException;
import com.techmarket.user_service.model.RefreshToken;
import com.techmarket.user_service.model.User;
import com.techmarket.user_service.repository.UserRepository;
import com.techmarket.user_service.service.JwtService;
import com.techmarket.user_service.service.RefreshTokenService;
import com.techmarket.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse register(AuthRequest authRequest) throws Exception{
        if (authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            throw new FunctionalException(MessageConstants.EMAIL_EMPTY);
        }
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()){
            throw new FunctionalException(MessageConstants.USER_EXISTS_EMAIL);
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(token, refreshToken.getToken());
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) throws Exception{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new FunctionalException(MessageConstants.USER_NOT_FOUND));
        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(token, refreshToken.getToken());
    }
}
