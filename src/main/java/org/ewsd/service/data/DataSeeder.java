package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.staff.Staff;
import org.ewsd.entity.user.User;
import org.ewsd.repository.role.RoleRepository;
import org.ewsd.repository.staff.StaffRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Override
    public void run(String... args) throws Exception {
        Role staffRole = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        Role tutorRole = roleRepository.findByName("TUTOR")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        User staff = User.builder()
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


//        STUDENT SEED
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(()->new RuntimeException("Student role not found"));

        User studentUser = User.builder()
                .email("student@example.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Student1")
                .accountNonLocked(true)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(studentRole))
                .customPermissions(new HashSet<>())
                .build();

        staffRepository.save(student);

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
        studentUser = userRepository.save(studentUser);

        Student student = Student.builder()
                .fullName("Student1")
                .user(studentUser)
                .build();

        studentRepository.save(student);
    }

}
