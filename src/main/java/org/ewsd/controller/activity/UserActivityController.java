package org.ewsd.controller.activity;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.analytics.ReportSummaryDTO;
import org.ewsd.entity.activity.UserActivity;
import org.ewsd.service.activity.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class UserActivityController {

    private final AnalyticsService analyticsService;

    @GetMapping("/report")
    public ResponseEntity<ReportSummaryDTO> getFullReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(analyticsService.getAdminReport(startDate, endDate));
    }
}