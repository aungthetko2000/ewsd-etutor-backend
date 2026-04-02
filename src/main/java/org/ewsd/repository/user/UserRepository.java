package org.ewsd.repository.user;

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
}
