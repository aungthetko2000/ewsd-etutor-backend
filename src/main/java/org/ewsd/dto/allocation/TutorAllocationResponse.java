package org.ewsd.dto.allocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorAllocationResponse {

    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long tutorId;
    private String tutorName;
    private String tutorEmail;
    private LocalDateTime assignedAt;
    private String message;

}
