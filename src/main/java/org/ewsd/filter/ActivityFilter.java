package org.ewsd.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ewsd.entity.activity.UserActivity;
import org.ewsd.repository.activity.UserActivityRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ActivityFilter extends OncePerRequestFilter {
    private final UserActivityRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isApi = uri.contains("/api/v1/");
        boolean isAnalytics = uri.contains("/admin/analytics");
        boolean isIgnoredApi = uri.contains("/chat") || uri.contains("/notifications");

        if (isApi && !isAnalytics && !isIgnoredApi) {
            logActivity(request, uri, auth.getName());
        }

        filterChain.doFilter(request, response);
    }

    private void logActivity(HttpServletRequest request, String uri, String username) {
        String userAgent = request.getHeader("User-Agent");
        String browser = "Other";
        if (userAgent != null) {
            if (userAgent.contains("Edg")) browser = "Edge";
            else if (userAgent.contains("Firefox")) browser = "Firefox";
            else if (userAgent.contains("Chrome")) browser = "Chrome";
            else if (userAgent.contains("Safari")) browser = "Safari";
        }

        UserActivity log = UserActivity.builder()
                .username(username)
                .browser(browser)
                .requestUri(uri)
                .action("PAGE_VIEW")
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(log);
    }
}