package org.ewsd.dto.student;

import lombok.Builder;
import lombok.Data;
import org.ewsd.entity.student.Student;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponseDto {

    private Long id;
    private String fullName;
    private Long currentTutorId;
    private boolean assigned;
    private String email;
    private Integer age;
    private String session;
    private String phone;
    private String address;
    private String course;
    private LocalDateTime registrationDate;

}