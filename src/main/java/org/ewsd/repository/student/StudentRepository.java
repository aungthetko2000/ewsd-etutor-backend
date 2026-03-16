package org.ewsd.repository.student;

import org.ewsd.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        select s
        from Student s
        where lower(s.fullName) like lower(concat('%', :name, '%'))
    """)
    List<Student> findAllByStudentName(@Param("name") String name);
}