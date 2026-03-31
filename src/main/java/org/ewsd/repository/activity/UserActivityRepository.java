package org.ewsd.repository.activity;

import org.ewsd.dto.analytics.ReportSummaryDTO;
import org.ewsd.entity.activity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    @Query("SELECT new org.ewsd.dto.analytics.ReportSummaryDTO$BrowserStat(u.browser, COUNT(u)) " +
            "FROM UserActivity u " +
            "WHERE u.timestamp BETWEEN :start AND :end " +
            "GROUP BY u.browser " +
            "ORDER BY COUNT(u) DESC")
    List<ReportSummaryDTO.BrowserStat> findBrowserStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new org.ewsd.dto.analytics.ReportSummaryDTO$PageStat(u.requestUri, COUNT(u)) " +
            "FROM UserActivity u " +
            "WHERE u.timestamp BETWEEN :start AND :end " +
            "GROUP BY u.requestUri " +
            "ORDER BY COUNT(u) DESC")
    List<ReportSummaryDTO.PageStat> findPageStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new org.ewsd.dto.analytics.ReportSummaryDTO$UserStat(u.username, COUNT(u)) " +
            "FROM UserActivity u " +
            "WHERE u.timestamp BETWEEN :start AND :end " +
            "AND u.username != 'Anonymous' " +
            "GROUP BY u.username " +
            "ORDER BY COUNT(u) DESC")
    List<ReportSummaryDTO.UserStat> findActiveUsers(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}