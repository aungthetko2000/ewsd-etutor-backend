package org.ewsd.repository.user;

import org.ewsd.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
