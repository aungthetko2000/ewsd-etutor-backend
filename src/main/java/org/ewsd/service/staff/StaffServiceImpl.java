package org.ewsd.service.staff;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.tutor.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public List<TutorAllocationResponse> bulkAllocateStudentsToTutor(List<Long> studentIds, Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor was not found"));

        List<Student> students = studentRepository.findAllById(studentIds);
        List<TutorAllocationResponse> responses = new ArrayList<>();

        for (Student student : students) {
            if (student.getTutor() != null &&
                    student.getTutor().getId().equals(tutorId)) {
                responses.add(TutorAllocationResponse.builder()
                        .studentId(student.getId())
                        .message("Student already assigned to this tutor")
                        .build());
                continue;
            }

            student.setTutor(tutor);

            responses.add(TutorAllocationResponse.builder()
                    .studentId(student.getId())
                    .studentName(student.getUser().getFirstName() + student.getUser().getLastName())
                    .studentEmail(student.getUser().getEmail())
                    .tutorId(tutor.getId())
                    .tutorName(tutor.getFullName())
                    .tutorEmail(tutor.getUser().getEmail())
                    .message("Student successfully assigned to tutor")
                    .build());
        }
        studentRepository.saveAll(students);
        return responses;
    }

    @Override
    public List<StudentResponseDto> getStudentsWithNoTutor() {
        return studentRepository.findByTutorIsNull()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private StudentResponseDto mapToDto(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getUser().getEmail())
                .currentTutorId(student.getTutor() != null ? student.getTutor().getId() : null)
                .assigned(student.getTutor() != null)
                .email(student.getUser().getEmail()) //NEW
                .age(student.getAge())       // NEW
                .session(student.getSession())   // NEW

                //New fields added
                .phone(student.getPhone())
                .address(student.getAddress())
                .status(student.getStatus())
                .course(student.getCourse())
                .registrationDate(student.getRegistrationDate())
                .build();
    }
}
