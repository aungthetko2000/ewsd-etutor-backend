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

        staff = userRepository.save(staff);

        Staff student = Staff.builder()
                .fullName("John Smith")
                .user(staff)
                .build();

        staffRepository.save(student);
    }

}
