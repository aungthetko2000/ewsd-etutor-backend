package org.ewsd.service.auth;

import org.ewsd.dto.login.LoginRequest;
import org.ewsd.dto.login.LoginResponse;
import org.ewsd.dto.token.RefreshTokenRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

}
