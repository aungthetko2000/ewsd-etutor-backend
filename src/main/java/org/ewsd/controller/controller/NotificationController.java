package org.ewsd.controller.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.notification.NotificationResponseDto;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.service.notification.NotificationService;
import org.ewsd.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('VIEW_NOTIFICATION')")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getAll(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<NotificationResponseDto> notifications = notificationService.getNotificationsForUser(email);
        ApiResponse<List<NotificationResponseDto>> response = ApiResponse.success(notifications, "Get all Notifications");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/handled")
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('UPDATE_NOTIFICATION')")
    public void markHandled(@PathVariable Long id) {
        notificationService.markHandled(id);
    }

    private String extractEmail(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.extractEmail(token);
    }
}
