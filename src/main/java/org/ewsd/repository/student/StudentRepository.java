package org.ewsd.repository.student;

import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public  interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByTutorIsNull();

    List<Student> findByTutorId(Long tutorId);

    @Query("""
        select distinct u.email
        from Student s
        join s.user u
        where s.tutor.user.id = :userId
          and lower(
            function('substring_index', u.email, '@', 1)
          ) like lower(concat(:email, '%'))
    """)
    List<String> findStudentEmailsByTutorAndEmailLike(@Param("userId") Long userId, @Param("email") String email);

    @Query(value = """
        SELECT s.*
        FROM students s
        JOIN users u ON s.user_id = u.id
        WHERE u.last_login_time <= :dateTime
        """, nativeQuery = true)
    List<Student> findInactiveStudents(LocalDateTime dateTime);
}