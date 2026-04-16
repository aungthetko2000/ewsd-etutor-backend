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

    @Override
    public void run(String... args) throws Exception {

        Role staffRole = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("STAFF role not found! Run data.sql first."));

        Role superStaffRole = roleRepository.findByName("AUTHORIZE_STAFF")
                .orElseThrow(() -> new RuntimeException("AUTHORIZE_STAFF role not found! Run data.sql first."));

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(()->new RuntimeException("Student role not found! Run data.sql first."));

        for (int i = 1; i <= 10; i++) {

            String email = "dev.aungthetko+student" + i + "@gmail.com";

            User studentUser = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Student")
                    .lastName(String.valueOf(i))
                    .accountNonLocked(true)
                    .enabled(true)
                    .accountNonExpired(true)
                    .credentialsNonExpired(true)
                    .roles(Set.of(studentRole))
                    .customPermissions(new HashSet<>())
                    .build();

            studentUser = userRepository.save(studentUser);

            Student student = Student.builder()
                    .user(studentUser)
                    .fullName(studentUser.getFirstName() + " " + studentUser.getLastName())
                    .age(18 + (i % 5)) // random age 18-22
                    .session("UOG-23")
                    .registrationDate(LocalDateTime.now())
                    .phone("0979518272" + i)
                    .address("Yangon")
                    .course("Diploma In Information")
                    .build();

            studentRepository.save(student);
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

        User autorizeStaff = User.builder()
                .email("superstaff@example.com")
                .password(passwordEncoder.encode("password123"))
                .firstName("Super")
                .lastName("Staff")
                .accountNonLocked(true)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(superStaffRole))
                .customPermissions(new HashSet<>())
                .build();
        autorizeStaff = userRepository.save(autorizeStaff);
        Staff superStaff = Staff.builder()
                .fullName(autorizeStaff.getFirstName() + autorizeStaff.getLastName())
                .user(autorizeStaff)
                .build();
        staffRepository.save(superStaff);

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
