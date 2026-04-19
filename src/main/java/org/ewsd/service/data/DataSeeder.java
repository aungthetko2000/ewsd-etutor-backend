package org.ewsd.service.data;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.admin.Admin;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.staff.Staff;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;
import org.ewsd.repository.admin.AdminRepository;
import org.ewsd.repository.role.RoleRepository;
import org.ewsd.repository.staff.StaffRepository;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
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
                .orElseThrow(() -> new RuntimeException("Student role not found! Run data.sql first."));

        for (int i = 1; i <= 10; i++) {

            final int index = i;
            String email = "dev.aungthetko+student" + i + "@gmail.com";

            User studentUser = userRepository.findByEmail(email)
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .email(email)
                                    .password(passwordEncoder.encode("password123"))
                                    .firstName("Student")
                                    .lastName(String.valueOf(index))
                                    .accountNonLocked(true)
                                    .enabled(true)
                                    .accountNonExpired(true)
                                    .credentialsNonExpired(true)
                                    .roles(Set.of(studentRole))
                                    .customPermissions(new HashSet<>())
                                    .build()
                    ));

            if (!studentRepository.existsByUser(studentUser)) {
                Student student = Student.builder()
                        .user(studentUser)
                        .fullName(studentUser.getFirstName() + " " + studentUser.getLastName())
                        .age(18 + (i % 5))
                        .session("UOG-23")
                        .registrationDate(LocalDate.now())
                        .phone("0979518272" + i)
                        .address("Yangon")
                        .course("Diploma In Information")
                        .build();

                studentRepository.save(student);
            }
        }

        // =========================
        // STAFF (SAFE)
        // =========================
        String staffEmail = "staff@example.com";

        User staffUser = userRepository.findByEmail(staffEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(staffEmail)
                                .password(passwordEncoder.encode("password123"))
                                .firstName("John")
                                .lastName("Staff")
                                .accountNonLocked(true)
                                .enabled(true)
                                .accountNonExpired(true)
                                .credentialsNonExpired(true)
                                .roles(Set.of(staffRole))
                                .customPermissions(new HashSet<>())
                                .build()
                ));

        if (!staffRepository.existsByUser(staffUser)) {
            Staff staff = Staff.builder()
                    .fullName(staffUser.getFirstName() + staffUser.getLastName())
                    .user(staffUser)
                    .build();
            staffRepository.save(staff);
        }

        // =========================
        // SUPER STAFF (SAFE)
        // =========================
        String superEmail = "superstaff@example.com";

        User superStaffUser = userRepository.findByEmail(superEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(superEmail)
                                .password(passwordEncoder.encode("password123"))
                                .firstName("Super")
                                .lastName("Staff")
                                .accountNonLocked(true)
                                .enabled(true)
                                .accountNonExpired(true)
                                .credentialsNonExpired(true)
                                .roles(Set.of(superStaffRole))
                                .customPermissions(new HashSet<>())
                                .build()
                ));

        if (!staffRepository.existsByUser(superStaffUser)) {
            Staff superStaff = Staff.builder()
                    .fullName(superStaffUser.getFirstName() + superStaffUser.getLastName())
                    .user(superStaffUser)
                    .build();
            staffRepository.save(superStaff);
        }

        // =========================
        // ADMIN (SAFE)
        // =========================
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found."));

        String adminEmail = "admin@etutor.com";

        User adminUser = userRepository.findByEmail(adminEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(adminEmail)
                                .password(passwordEncoder.encode("admin123"))
                                .firstName("System")
                                .lastName("Admin")
                                .roles(Set.of(adminRole))
                                .enabled(true)
                                .accountNonLocked(true)
                                .accountNonExpired(true)
                                .credentialsNonExpired(true)
                                .build()
                ));

        if (!adminRepository.existsByUser(adminUser)) {
            Admin adminProfile = Admin.builder()
                    .fullName(adminUser.getFirstName() + " " + adminUser.getLastName())
                    .user(adminUser)
                    .build();

            adminRepository.save(adminProfile);
        }
    }
}
