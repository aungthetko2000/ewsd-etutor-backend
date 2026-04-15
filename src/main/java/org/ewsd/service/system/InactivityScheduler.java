package org.ewsd.service.system;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.email.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class InactivityScheduler {
//
//    private final UserRepository userRepository;
//    private final EmailService emailService;
//
//    @Scheduled(fixedRate = 30000)
//    public void checkInactiveUsers() {
//
//        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);
//
//        List<User> users = userRepository.findInactiveUsers(threshold);
//
//        for (User user : users) {
//            emailService.sendInactivityEmail(user.getEmail());
//        }
//    }
//}