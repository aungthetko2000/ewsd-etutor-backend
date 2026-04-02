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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public StudentResponseDto registerStudent(StudentRegisterRequest request) {

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Student role not found"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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
                .age(request.getAge())
                .grade(request.getGrade())
                .user(user)
                .build();

        studentRepository.save(student);

        return mapToDto(student);
    }

}