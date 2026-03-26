package org.ewsd.dto.student;

import lombok.Builder;
import lombok.Data;
import org.ewsd.entity.student.Student;

@Data
@Builder
public class StudentResponseDto {

    private Long id;
    private String fullName;
    private Long currentTutorId;
    private boolean assigned;
    private String email;
    private String eduEmail; // student login email

    private Integer age;
    private String grade;
}