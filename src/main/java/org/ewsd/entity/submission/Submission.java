package org.ewsd.entity.submission;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assignmentId;
    private Long studentId;
    private String filePath;
    private LocalDateTime uploadTimestamp;
}