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
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Order(2) //test assigned students
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


        List<User> userLists = List.of(
                new User(null, "mgmg@example.com", passwordEncoder.encode("password")
                , "Mg", "Mg", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole),
                        new HashSet<>(), null, null, null, null, null),
                new User(null, "aungaung@example.com", passwordEncoder.encode("password"),
                        "Aung", "Aung", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "kyaw@example.com", passwordEncoder.encode("password")
                        , "Kyaw", "Kyaw", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole),
                        new HashSet<>(), null, null, null, null, null),
                new User(null, "hla@example.com", passwordEncoder.encode("password"),
                        "Hla", "Hla", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(), null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "su@example.com", passwordEncoder.encode("password"),
                "Su", "Su", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "moe@example.com", passwordEncoder.encode("password"),
                "Moe", "Moe", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "zaw@example.com", passwordEncoder.encode("password"),
                        "Zaw", "Zaw", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "ko@example.com", passwordEncoder.encode("password"),
                        "Ko", "Ko", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null,null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null),
                new User(null, "myoaung@example.com", passwordEncoder.encode("password"),
                        "Myo", "Aung", true, true, true, true, LocalDateTime.now(), LocalDateTime.now(),null, null, Set.of(studentRole)
                        , new HashSet<>(), null, null, null, null, null)
        );

        List<User> savedUser = userRepository.saveAll(userLists);

        List<Tutor> tutors = tutorRepository.findAll();
        Tutor firstTutor = tutors.get(0);

        List<Student> studentList = savedUser.stream().map(
                user -> Student.builder()
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .age(16)
                        .grade("Grade 10")
                        .user(user)
                        .tutor(firstTutor)
                        .build()
        ).toList();

        studentRepository.saveAll(studentList);

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
    }

}
