package org.ewsd.entity.student;

import jakarta.persistence.*;
import lombok.*;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Meeting> meetings = new ArrayList<>();
}