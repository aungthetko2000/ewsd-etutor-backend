package org.ewsd.entity.student;

import jakarta.persistence.*;
import lombok.*;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.submission.Submission;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String fatherName;
    private LocalDate dob;
    private String gender;
    private Integer age;
    private String session;
    private String emergencyContact;
    private String address;
    private LocalDate registrationDate;
    private String phone;
    private String course;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Submission> submissions;

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDate.now();
    }
}