package org.ewsd.service.activity;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.analytics.ReportSummaryDTO;
import org.ewsd.repository.activity.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UserActivityRepository repository;

    public ReportSummaryDTO getAdminReport(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return ReportSummaryDTO.builder()
                .topBrowsers(repository.findBrowserStats(since))
                .topPages(repository.findPageStats(since))
                .activeUsers(repository.findActiveUsers(since))
                .build();
    }
}