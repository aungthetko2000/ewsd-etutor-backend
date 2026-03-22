package org.ewsd.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ewsd.entity.activity.UserActivity;
import org.ewsd.repository.activity.UserActivityRepository;
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
        String method = request.getMethod();

        if (uri.contains("/api/v1/")) {

            String userAgent = request.getHeader("User-Agent");
            String browser = "Other";

            if (userAgent != null) {
                if (userAgent.contains("Edg")) browser = "Edge";
                else if (userAgent.contains("Chrome")) browser = "Chrome";
                else if (userAgent.contains("Firefox")) browser = "Firefox";
                else if (userAgent.contains("Safari")) browser = "Safari";
            }

            String currentUsername = "Anonymous";
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                currentUsername = auth.getName();
            }

            if ((uri.contains("/login") && method.equals("POST")) || method.equals("POST") || method.equals("PUT")) {
                UserActivity log = UserActivity.builder()
                        .username(currentUsername)
                        .browser(browser)
                        .action(method + " " + uri)
                        .timestamp(LocalDateTime.now())
                        .build();
                repository.save(log);
            }
        }
        filterChain.doFilter(request, response);
    }
}