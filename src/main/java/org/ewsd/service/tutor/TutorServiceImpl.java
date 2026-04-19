package org.ewsd.service.tutor;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.tutor.TutorRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.ewsd.repository.student.StudentRepository;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

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
    public List<StudentResponseDto> getAssignedStudents(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Tutor was not found"));

        return studentRepository.findByTutorId(user.getTutor().getId())
                .stream()
                .map(student -> StudentResponseDto.builder()
                        .id(student.getId())
                        .fullName(student.getFullName())
                        .email(student.getUser().getEmail())
                        .currentTutorId(student.getTutor() != null ? student.getTutor().getId() : null)
                        .assigned(student.getTutor() != null)
                        .age(student.getAge())
                        .session(student.getSession())
                        .build())
                .toList();
    }
}