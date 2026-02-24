package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.role.Role;
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
public class TutorDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    @Override
    public void run(String... args) throws Exception {

        Role tutorRole = roleRepository.findByName("TUTOR")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        List<User> tutorUsers = List.of(
                User.builder()
                        .email("tutor@example.com")
                        .password(passwordEncoder.encode("password123"))
                        .fullName("Tony Stark")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(tutorRole))
                        .customPermissions(new HashSet<>())
                        .build(),

                User.builder()
                        .email("tutor2@example.com")
                        .password(passwordEncoder.encode("password456"))
                        .fullName("Bruce Banner")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(tutorRole))
                        .customPermissions(new HashSet<>())
                        .build(),

                User.builder()
                        .email("tutor3@example.com")
                        .password(passwordEncoder.encode("password789"))
                        .fullName("Steve Rogers")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(tutorRole))
                        .customPermissions(new HashSet<>())
                        .build(),

                User.builder()
                        .email("tutor4@example.com")
                        .password(passwordEncoder.encode("password012"))
                        .fullName("Clint Barton")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(tutorRole))
                        .customPermissions(new HashSet<>())
                        .build(),

                User.builder()
                        .email("tutor5@example.com")
                        .password(passwordEncoder.encode("password345"))
                        .fullName("Thor Odinson")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(tutorRole))
                        .customPermissions(new HashSet<>())
                        .build()
        );

        List<User> savedUser = userRepository.saveAll(tutorUsers);

        List<Tutor> tutorList = savedUser.stream()
                .map(user -> Tutor.builder()
                        .fullName(user.getFullName())
                        .user(user).build())
                .toList();

        tutorRepository.saveAll(tutorList);

    }

}
