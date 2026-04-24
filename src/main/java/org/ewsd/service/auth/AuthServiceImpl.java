package org.ewsd.service.auth;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.login.LoginRequest;
import org.ewsd.dto.login.LoginResponse;
import org.ewsd.dto.token.RefreshTokenRequest;
import org.ewsd.entity.user.User;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.user.UserService;
import org.ewsd.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByEmail(request.getEmail());

        LocalDateTime now = LocalDateTime.now();
        user.setPreviousLoginTime(user.getLastLoginTime());
        user.setLastLoginTime(now);
        userRepository.save(user);

        List<String> permissions = userService.getUserPermissions(request.getEmail());
        List<String> roles = userService.getUserRoles(request.getEmail());

        String accessToken = jwtUtil.generateAccessToken(user, userDetails, permissions, roles);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'at' hh:mm a");
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpirationTimeInSecond())
                .permissions(permissions)
                .roles(roles)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .lastLoginTime(user.getLastLoginTime().format(formatter))
                        .previousLoginTime(user.getPreviousLoginTime() == null ? null : user.getPreviousLoginTime().format(formatter))
                        .build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(request.getRefreshToken(), userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userService.getUserByEmail(username);

        List<String> permissions = userService.getUserPermissions(username);
        List<String> roles = userService.getUserRoles(username);
        String newAccessToken = jwtUtil.generateAccessToken(user, userDetails, permissions, roles);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpirationTimeInSecond())
                .permissions(permissions)
                .roles(roles)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .build();
    }
}
