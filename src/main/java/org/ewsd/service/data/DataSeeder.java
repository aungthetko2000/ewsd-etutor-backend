package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.staff.Staff;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.role.RoleRepository;
import org.ewsd.repository.staff.StaffRepository;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.tutor.TutorRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    @Override
    public void run(String... args) throws Exception {
        Role staffRole = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        Role tutorRole = roleRepository.findByName("TUTOR")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(()->new RuntimeException("Student role not found"));

        User staffUser = User.builder()
                .email("staff@example.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("John Staff")
                .accountNonLocked(true)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(staffRole))
                .customPermissions(new HashSet<>())
                .build();

        staffUser = userRepository.save(staffUser);
        Staff staff = Staff.builder()
                .fullName("John Smith")
                .user(staffUser)
                .build();
        staffRepository.save(staff);

        List<User> userLists = List.of(
                new User(null, "student1@example.com", passwordEncoder.encode("password")
                , "Alice", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                        new HashSet<>(), null, null),
                new User(null, "student2@example.com", passwordEncoder.encode("password")
                        , "Daniel", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                        new HashSet<>(), null, null)
        );

        List<User> savedUser = userRepository.saveAll(userLists);

        List<Student> studentList = savedUser.stream().map(
                user -> Student.builder()
                        .fullName(user.getFullName())
                        .user(user).build()
        ).toList();

        studentRepository.saveAll(studentList);


        User tutorUser = User.builder()
                .email("tutor@example.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Daniel Tutor")
                .accountNonLocked(true)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(tutorRole))
                .customPermissions(new HashSet<>())
                .build();

        tutorUser = userRepository.save(tutorUser);

        Tutor tutor = Tutor.builder()
                .fullName("Astro Smith")
                .user(tutorUser)
                .build();
        tutorRepository.save(tutor);

    }

}
