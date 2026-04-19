package org.ewsd.entity.note;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.meeting.Meeting;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String sessionNote;

    @OneToOne
    @JoinColumn(name = "meeting_id", unique = true)
    private Meeting meeting;
}
