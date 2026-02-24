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

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(()->new RuntimeException("Student role not found"));

        if (studentRepository.count() == 0) {

            String[] studentNames = {
                    "Mg Mg",
                    "Aung Aung",
                    "Kyaw Kyaw",
                    "Aye Aye",
                    "Hla Hla",
                    "Su Su",
                    "Moe Moe",
                    "Zaw Zaw",
                    "Ko Ko",
                    "Thura"
            };

            for (int i = 0; i < studentNames.length; i++) {

                User user = User.builder()
                        .email("student" + i + "@example.com")
                        .password(passwordEncoder.encode("password123"))
                        .fullName(studentNames[i])
                        .accountNonLocked(true)
                        .enabled(true)
                        .accountNonExpired(true)
                        .credentialsNonExpired(true)
                        .roles(Set.of(studentRole))
                        .customPermissions(new HashSet<>())
                        .build();

                user = userRepository.save(user);

                Student student = Student.builder()
                        .fullName(studentNames[i])
                        .user(user)
                        .build();   // tutor is NULL → Unassigned

                studentRepository.save(student);
            }
        }

        User staffUser = User.builder()
                .email("staff@example.com")
                .password(passwordEncoder.encode("password123"))
                .firstName("John")
                .lastName("Staff")
                .accountNonLocked(true)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(staffRole))
                .customPermissions(new HashSet<>())
                .build();

        staffUser = userRepository.save(staffUser);
        Staff staff = Staff.builder()
                .fullName(staffUser.getFirstName() + staffUser.getLastName())
                .user(staffUser)
                .build();
        staffRepository.save(staff);

        List<User> userLists = List.of(
                new User(null, "student1@example.com", passwordEncoder.encode("password")
                , "Alice", "Wonderland", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                new User(null, "alice@example.com", passwordEncoder.encode("password")
                , "Alice", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                        new HashSet<>(), null, null),
                new User(null, "student2@example.com", passwordEncoder.encode("password")
                        , "Daniel", "Smith", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                new User(null, "daniel@example.com", passwordEncoder.encode("password")
                        , "Daniel", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                        new HashSet<>(), null, null)
        );

        List<User> savedUser = userRepository.saveAll(userLists);

        List<Student> studentList = savedUser.stream().map(
                user -> Student.builder()
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .user(user).build()
        ).toList();

        studentRepository.saveAll(studentList);


    }

}
