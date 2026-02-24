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

    private StudentResponseDto mapToDto(Student student) {

        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
//                .avatarUrl(student.getAvatarUrl())
                .currentTutorId(
                        student.getTutor() != null ? student.getTutor().getId() : null
                )
                .assigned(student.getTutor() != null)
                .build();
    }

}