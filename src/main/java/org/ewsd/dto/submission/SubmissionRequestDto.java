package org.ewsd.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionRequestDto {

    private LocalDateTime uploadTimestamp;
    private Long assignmentId;

}
