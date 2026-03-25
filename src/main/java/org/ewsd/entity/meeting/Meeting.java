package org.ewsd.entity.meeting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.enumeration.MeetingStatus;
import org.ewsd.enumeration.MeetingType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDate scheduledAt;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meeting_students",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    @Column(name = "meeting_type", nullable = false)
    private MeetingType meetingType;

    @Column(name = "session_color", nullable = false)
    private String sessionColor;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    private String location;

    @Column(name = "virtual_platform")
    private String virtualPlatform;

    @Column(name = "virtual_platform_link")
    private String virtualPlatformLink;

    @Column(name = "reason")
    private String reason;
}
