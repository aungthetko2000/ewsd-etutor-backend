package org.ewsd.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.NotificationStatus;
import org.ewsd.enumeration.NotificationType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(nullable = false, updatable = false)
    private LocalDate scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    // Reference to the meeting that triggered this notification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
}
