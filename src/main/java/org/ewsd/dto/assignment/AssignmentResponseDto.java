package org.ewsd.dto.assignment;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignmentResponseDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String uploadedBy;
    private String subject;

}