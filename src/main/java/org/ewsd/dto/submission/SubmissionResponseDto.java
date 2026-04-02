package org.ewsd.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionResponseDto {

    private Long id;
    private String filePath;
    private LocalDateTime uploadTimestamp;
    private LocalDate dueDate;
    private String description;
    private String status;
    private String fileName;
    private Long assignmentId;

}
