package org.ewsd.dto.analytics;

import lombok.*;
import java.util.List;

@Data
@Builder
public class ReportSummaryDTO {
    private List<BrowserStat> topBrowsers;
    private List<PageStat> topPages;
    private List<UserStat> activeUsers;

    @Data @AllArgsConstructor
    public static class BrowserStat { private String browser; private Long count; }

    @Data @AllArgsConstructor
    public static class PageStat { private String page; private Long count; }

    @Data @AllArgsConstructor
    public static class UserStat { private String username; private Long count; }
}