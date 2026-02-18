package org.ewsd.repository.student;

import org.ewsd.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface StudentRepository extends JpaRepository<Student, Long> {
}