package org.ewsd.service.student;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.repository.student.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentResponseDto> getStudents(Boolean unassignedOnly) {

        List<Student> students;

        if (Boolean.TRUE.equals(unassignedOnly)) {
            students = studentRepository.findByTutorIsNull();
        } else {
            students = studentRepository.findAll();
        }

        return students.stream()
                .map(this::mapToDto)
                .toList();
    }

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