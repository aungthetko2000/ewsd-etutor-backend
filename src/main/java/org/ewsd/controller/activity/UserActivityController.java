package org.ewsd.controller.activity;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.activity.UserActivity;
import org.ewsd.service.activity.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class UserActivityController {

    private final AnalyticsService analyticsService;

    @GetMapping("/report")
    public ResponseEntity<?> getFullReport() {
        return ResponseEntity.ok(analyticsService.getAdminReport());
    }
}