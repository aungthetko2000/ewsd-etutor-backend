package org.ewsd.service.activity;

import lombok.RequiredArgsConstructor;
import org.ewsd.repository.activity.UserActivityRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UserActivityRepository repository;

    public Map<String, Object> getAdminReport() {
        Map<String, Object> report = new LinkedHashMap<>();

        String browser = repository.findMostUsedBrowser();
        String user = repository.findMostActiveUser();

        report.put("mostUsedBrowser", browser != null ? browser : "No data yet");
        report.put("mostActiveUser", user != null ? user : "No data yet");

        Map<String, Long> browserStats = new HashMap<>();
        repository.getBrowserUsageStats().forEach(row ->
                browserStats.put(row[0] != null ? (String) row[0] : "Unknown", (Long) row[1]));
        report.put("browserUsageDetail", browserStats);

        return report;
    }
}