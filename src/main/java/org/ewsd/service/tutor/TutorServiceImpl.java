package org.ewsd.service.tutor;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.repository.tutor.TutorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.ewsd.repository.student.StudentRepository;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<TutorResponse> getAllTutors() {
        return tutorRepository.findAll().stream()
                .map(tutor -> TutorResponse.builder()
                        .id(tutor.getId())
                        .fullName(tutor.getFullName())
                        .email(tutor.getUser().getEmail())
                        .expertise(tutor.getExpertise())
                        .build())
                .toList();
    }

    @Override
    public List<StudentResponseDto> getAssignedStudents(Long tutorId) {

        return studentRepository.findByTutorId(tutorId)
                .stream()
                .map(student -> StudentResponseDto.builder()
                        .id(student.getId())
                        .fullName(student.getFullName())
                        .email(student.getUser().getEmail())
                        .currentTutorId(tutorId)
                        .assigned(true)
                        .age(student.getAge())
                        .grade(student.getGrade())
                        .build())
                .toList();
    }
}