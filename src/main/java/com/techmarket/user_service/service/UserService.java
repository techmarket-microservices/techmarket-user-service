package com.techmarket.user_service.service;

import com.techmarket.user_service.dto.request.AuthRequest;
import com.techmarket.user_service.dto.response.AuthResponse;

public interface UserService {
    AuthResponse register(AuthRequest authRequest) throws Exception;
    AuthResponse login(AuthRequest authRequest) throws Exception;
}
