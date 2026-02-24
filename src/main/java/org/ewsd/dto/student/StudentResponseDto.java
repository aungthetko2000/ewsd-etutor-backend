package org.ewsd.dto.student;

import lombok.Builder;
import lombok.Data;
import org.ewsd.entity.student.Student;

@Data
@Builder
public class StudentResponseDto {

    private Long id;
    private String fullName;
    private String avatarUrl;
    private Long currentTutorId;
    private boolean assigned;

}