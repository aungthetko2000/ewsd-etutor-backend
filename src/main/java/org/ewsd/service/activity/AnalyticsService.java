package org.ewsd.service.activity;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.analytics.ReportSummaryDTO;
import org.ewsd.repository.activity.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UserActivityRepository repository;

    public ReportSummaryDTO getAdminReport(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(7);
        if (endDate == null) endDate = LocalDate.now();

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return ReportSummaryDTO.builder()
                .topBrowsers(repository.findBrowserStats(start, end))
                .topPages(repository.findPageStats(start, end))
                .activeUsers(repository.findActiveUsers(start, end))
                .build();
    }
}