package org.ewsd.repository.user;

import org.ewsd.dto.report.TutorMessageAverageResponse;
import org.ewsd.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByEmailIn(List<String> email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u
    WHERE u.lastLoginTime <= :date
    """)
    List<User> findInactiveUsers(LocalDateTime date);

    @Query("""
        select u
        from User u
        left join fetch u.student s
        left join fetch u.tutor t
        where lower(u.firstName) like lower(concat('%', :name, '%'))
           or lower(u.lastName) like lower(concat('%', :name, '%'))
    """)
    List<User> searchUsers(@Param("name") String name);

    @Query("""
        SELECT new org.ewsd.dto.report.TutorMessageAverageResponse(
            u.id,
            CONCAT(u.firstName, ' ', u.lastName),
            COUNT(m.id),
            COUNT(DISTINCT m.receiver.id),
            CASE 
                WHEN COUNT(DISTINCT m.receiver.id) = 0 THEN 0.0
                ELSE (COUNT(m.id) * 1.0 / COUNT(DISTINCT m.receiver.id))
            END
        )
        FROM User u
        LEFT JOIN Message m ON m.sender.id = u.id
        WHERE u.tutor IS NOT NULL
        GROUP BY u.id, u.firstName, u.lastName
        ORDER BY COUNT(m.id) DESC
        """)
    List<TutorMessageAverageResponse> getTutorMessageAverages();
}
