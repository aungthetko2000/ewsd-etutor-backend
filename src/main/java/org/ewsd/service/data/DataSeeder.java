package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.admin.Admin;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.staff.Staff;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.admin.AdminRepository;
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
import java.util.ArrayList;
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
    private final AdminRepository adminRepository;
    private final TutorRepository tutorRepository;

    @Override
    public void run(String... args) throws Exception {
        Role staffRole = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("STUDENT role not found! Run data.sql first."));

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(()->new RuntimeException("Student role not found"));

        LocalDateTime now = LocalDateTime.now();

        List<User> users = List.of(
                User.builder()
                        .email("mgmg@example.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Mg")
                        .lastName("Mg")
                        .createdAt(now)
                        .updatedAt(now)
                        .roles(Set.of(studentRole))
                        .build()
        );

        List<User> savedUsers = userRepository.saveAll(users);

        List<Tutor> tutors = tutorRepository.findAll();
        Tutor firstTutor = tutors.isEmpty() ? null : tutors.get(0);

        List<Student> studentList = new ArrayList<>();

        for (int i = 0; i < savedUsers.size(); i++) {

            User user = savedUsers.get(i);

            Student student = Student.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .age(23)
                    .grade("Final Year")
                    .eduEmail(user.getFirstName().toLowerCase() + "@university.edu")
                    .email(user.getEmail())
                    .session("Morning")
                    .registrationDate(now)
                    .phone("09123456789")
                    .address("Yangon")
                    .status("ACTIVE")
                    .course("Computing")
                    .user(user)
                    .tutor(i == 0 ? null : firstTutor) // first student unassigned
                    .build();

            user.setStudent(student);

            studentList.add(student);
        }

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

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));

        User adminUser = User.builder()
                .email("admin@etutor.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("System")
                .lastName("Admin")
                .roles(Set.of(adminRole))
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        adminUser = userRepository.save(adminUser);

        Admin adminProfile = Admin.builder()
                .fullName(adminUser.getFirstName() + " " + adminUser.getLastName())
                .user(adminUser)
                .build();

        adminRepository.save(adminProfile);
    }
}
