package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.role.RoleRepository;
import org.ewsd.repository.tutor.TutorRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Order(1) //test assigned students
@Service
@RequiredArgsConstructor
public class TutorDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;

    @Override
    public void run(String... args) {

        Role tutorRole = roleRepository.findByName("TUTOR")
                .orElseThrow(() -> new RuntimeException("TUTOR role not found!"));

        List<String> expertiseList = List.of(
                "Computer Science",
                "Science",
                "Mathematics",
                "English",
                "History"
        );

        List<User> tutorUsers = List.of(
                User.builder()
                        .email("tutor@example.com")
                        .password(passwordEncoder.encode("password123"))
                        .firstName("Tony")
                        .lastName("Stark")
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
                        .firstName("Bruce")
                        .lastName("Banner")
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
                        .firstName("Steve")
                        .lastName("Rogers")
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
                        .firstName("Clint")
                        .lastName("Barton")
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
                        .firstName("Thor").
                        lastName("Odinson")
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


        List<User> savedUsers = userRepository.saveAll(tutorUsers);

        List<Tutor> tutorList = IntStream.range(0, savedUsers.size())
                .mapToObj(i -> Tutor.builder()
                        .fullName(savedUsers.get(i).getFirstName() + " " + savedUsers.get(i).getLastName())
                        .expertise(expertiseList.get(i))
                        .user(savedUsers.get(i))
                        .build())
                .toList();

        tutorRepository.saveAll(tutorList);
    }
}