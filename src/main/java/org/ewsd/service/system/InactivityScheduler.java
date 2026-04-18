package org.ewsd.service.system;

import lombok.RequiredArgsConstructor;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.email.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InactivityScheduler {

    private final StudentRepository studentRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Yangon")
    public void checkInactiveStudents() {

        LocalDateTime threshold = LocalDateTime.now().minusDays(28);

        List<Student> students = studentRepository.findInactiveStudents(threshold);

        for (Student student : students) {
            emailService.sendInactivityEmail(student.getUser().getEmail(), student.getFullName());

            if (student.getTutor() != null) {
                emailService.sendTutorWarningEmail(student.getTutor().getUser().getEmail(),
                        student.getTutor().getFullName(), student.getFullName());
            }
        }
    }
}