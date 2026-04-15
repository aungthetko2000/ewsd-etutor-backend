package org.ewsd.service.staff;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.tutor.TutorRepository;
import org.ewsd.service.email.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public List<TutorAllocationResponse> bulkAllocateStudentsToTutor(
            List<Long> studentIds,
            Long tutorId
    ) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

        List<Student> students = studentRepository.findAllById(studentIds);

        if (students.size() != studentIds.size()) {
            throw new IllegalArgumentException("Some students were not found");
        }

        List<TutorAllocationResponse> responses = new ArrayList<>();
        List<Student> studentsToEmail = new ArrayList<>();

        for (Student student : students) {

            String message;

            if (student.getTutor() == null) {
                student.setTutor(tutor);
                message = "Student successfully allocated";
                studentsToEmail.add(student);

            } else if (student.getTutor().getId().equals(tutorId)) {
                message = "Student already assigned to this tutor";

            } else {
                student.setTutor(tutor);
                message = "Student successfully reallocated";
                studentsToEmail.add(student);
            }

            responses.add(
                    TutorAllocationResponse.builder()
                            .studentId(student.getId())
                            .studentName(student.getFullName())
                            .studentEmail(student.getUser().getEmail())
                            .tutorId(tutor.getId())
                            .tutorName(tutor.getFullName())
                            .tutorEmail(tutor.getUser().getEmail())
                            .message(message)
                            .build()
            );
        }

        studentRepository.saveAll(students);

        for (Student student : studentsToEmail) {
            emailService.sendReallocationMailToStudent(student, tutor);
        }

        return responses;
    }

    @Override
    public List<StudentResponseDto> getStudentsWithNoTutor() {
        return studentRepository.findByTutorIsNull()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TutorAllocationResponse> getAllAllocations() {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .filter(student -> student.getTutor() != null)
                .map(student -> TutorAllocationResponse.builder()
                        .studentId(student.getId())
                        .studentName(student.getUser().getFirstName() + " " + student.getUser().getLastName())
                        .studentEmail(student.getUser().getEmail())
                        .tutorId(student.getTutor().getId())
                        .tutorName(student.getTutor().getFullName())
                        .tutorEmail(student.getTutor().getUser().getEmail())
                        .build())
                .toList();
    }

    @Override
    public List<StudentResponseDto> getInactiveStudentsReport(Integer days) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        return studentRepository.findInactiveStudents(dateTime).stream()
                .map(this::mapToDto)
                .toList();
    }

    private StudentResponseDto mapToDto(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getUser().getEmail())
                .currentTutorId(student.getTutor() != null ? student.getTutor().getId() : null)
                .assigned(student.getTutor() != null)
                .email(student.getUser().getEmail())
                .age(student.getAge())
                .session(student.getSession())
                .phone(student.getPhone())
                .address(student.getAddress())
                .course(student.getCourse())
                .registrationDate(student.getRegistrationDate())
                .build();
    }
}
