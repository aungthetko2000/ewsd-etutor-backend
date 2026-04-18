package org.ewsd.service.student;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentRegisterRequest;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;
import org.ewsd.repository.role.RoleRepository;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.email.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ewsd.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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

    public StudentResponseDto registerStudent(StudentRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String rawPassword = generateRandomPassword();
        Role studentRole = roleRepository.findByName("STUDENT").orElseThrow(() -> new RuntimeException("Student role not found"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(studentRole))
                .customPermissions(new HashSet<>())
                .build();

        user = userRepository.save(user);

        Student student = Student.builder()
                .fullName(request.getFirstName() + " " + request.getLastName())
                .fatherName(request.getFatherName())
                .dob(request.getDob())
                .gender(request.getGender())
                .session(request.getSession())
                .emergencyContact(request.getEmergencyContact())
                .address(request.getAddress())
                .registrationDate(request.getRegistrationDate())
                .phone(request.getPhone())
                .course(request.getCourse())
                .user(user)
                .build();

        Student savedStudent = studentRepository.save(student);
        if (!savedStudent.getUser().getEmail().isBlank()) {
            emailService.sendSuccessRegisterMailToStudent(student, rawPassword);
        }
        return mapToDto(savedStudent);
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

}

