package org.ewsd.service.user;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.permission.Permission;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.entity.role.Role;
import org.ewsd.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User was not found"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found with email" + email));
    }

    @Transactional(readOnly = true)
    public List<String> getUserPermissions(String email) {
        User user = getUserByEmail(email);
        return user.getAllPermissions()
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getUserRoles(String email) {
        User user = getUserByEmail(email);
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
