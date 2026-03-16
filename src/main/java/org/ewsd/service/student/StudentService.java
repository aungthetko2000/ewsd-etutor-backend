package org.ewsd.service.student;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.repository.student.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<StudentResponseDto> getAssignedStudents(Long tutorId) {
        List<Student> list = studentRepository.findByTutorId(tutorId);
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private StudentResponseDto mapToDto(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getUser().getEmail())
                .avatarUrl(student.getAvatarUrl())
                .currentTutorId(student.getTutor() != null ? student.getTutor().getId() : null)
                .assigned(student.getTutor() != null)
                .build();
    }

}