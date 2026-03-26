package org.ewsd.repository.activity;

import org.ewsd.entity.activity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    @Query(value = "SELECT browser FROM user_activities GROUP BY browser ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    String findMostUsedBrowser();

    @Query(value = "SELECT username FROM user_activities WHERE username IS NOT NULL GROUP BY username ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    String findMostActiveUser();

    @Query(value = "SELECT browser, COUNT(*) FROM user_activities GROUP BY browser", nativeQuery = true)
    List<Object[]> getBrowserUsageStats();

    @Query(value = "SELECT username, COUNT(*) FROM user_activities WHERE username IS NOT NULL GROUP BY username ORDER BY COUNT(*) DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getTopActiveUsers();
}