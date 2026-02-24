package org.ewsd.entity.tutor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutors")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "tutor")
    private List<Student> assignedStudents = new ArrayList<>();

}