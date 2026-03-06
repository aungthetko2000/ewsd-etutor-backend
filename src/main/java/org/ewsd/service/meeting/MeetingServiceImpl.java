package org.ewsd.service.meeting;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ewsd.constants.SecurityConstants;
import org.ewsd.dto.meeting.MeetingConfirmationRequest;
import org.ewsd.dto.schedule.MeetingRequestDto;
import org.ewsd.dto.schedule.MeetingResponseDto;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.MeetingStatus;
import org.ewsd.enumeration.NotificationType;
import org.ewsd.repository.meeting.MeetingRepository;
import org.ewsd.repository.student.StudentRepository;
import org.ewsd.repository.tutor.TutorRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.notification.NotificationService;
import org.ewsd.util.JwtUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public MeetingResponseDto createMeeting(MeetingRequestDto dto, HttpServletRequest request) {

        String bearer = request.getHeader("Authorization");
        String token = bearer.substring(7);
        String email = jwtUtil.extractEmail(token);

        User tutorUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

        List<User> studentUsers = userRepository.findAllByEmailIn(dto.getStudentEmail());

        if (studentUsers.size() != dto.getStudentEmail().size()) {
            throw new IllegalArgumentException("One or more students were not found");
        }

        List<Student> students = studentUsers.stream()
                .map(User::getStudent)
                .collect(Collectors.toList());

        Meeting meeting = Meeting.builder()
                .meetingTitle(dto.getMeetingTitle())
                .description(dto.getDescription())
                .scheduledAt(dto.getScheduledAt())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .meetingType(dto.getMeetingType())
                .sessionColor(dto.getSessionColor())
                .tutor(tutorUser.getTutor())
                .students(students)
                .status(MeetingStatus.PENDING)
                .location(dto.getLocation())
                .virtualPlatform(dto.getVirtualPlatform())
                .virtualPlatformLink(dto.getVirtualPlatformLink())
                .build();

        Meeting savedMeeting = meetingRepository.save(meeting);
        MeetingResponseDto response = mapToResponse(savedMeeting);

        studentUsers.forEach(studentUser -> {
            String message = String.format("New meeting scheduled: '%s' on %s",
                    savedMeeting.getMeetingTitle(),
                    savedMeeting.getScheduledAt());

            notificationService.sendAndSave(
                    studentUser,
                    savedMeeting,
                    NotificationType.MEETING_SCHEDULED,
                    message
            );
        });

        return response;
    }

    @Override
    public List<MeetingResponseDto> getAllMeeting(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        String token = bearer.substring(7);
        String email = jwtUtil.extractEmail(token);
        return meetingRepository.findByTutor_User_Email(email).stream()
                .map(meeting -> MeetingResponseDto.builder()
                        .id(meeting.getId())
                        .tutorId(meeting.getTutor().getId())
                        .meetingTitle(meeting.getMeetingTitle())
                        .scheduledAt(meeting.getScheduledAt())
                        .startTime(meeting.getStartTime())
                        .endTime(meeting.getEndTime())
                        .description(meeting.getDescription())
                        .students(meeting.getStudents()
                                .stream()
                                .map(this::mapToDto)
                                .collect(Collectors.toList()))
                        .meetingType(meeting.getMeetingType())
                        .location(meeting.getLocation())
                        .virtualPlatform(meeting.getVirtualPlatform())
                        .virtualPlatformLink(meeting.getVirtualPlatformLink())
                        .sessionColor(meeting.getSessionColor())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllStudentEmailByTutor(Long userId, String email) {
        return studentRepository.findStudentEmailsByTutorAndEmailLike(userId, email);
    }

    @Override
    @Transactional
    public void updateMeetingStatus(Long id, MeetingConfirmationRequest request) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meeting was not found"));

        if (request.getMeetingStatus() == MeetingStatus.PENDING) {
            return;
        }
        if (request.getMeetingStatus() == MeetingStatus.CONFIRMED) {
            meeting.setStatus(MeetingStatus.CONFIRMED);
        } else {
            meeting.setReason(request.getReason());
            meeting.setStatus(MeetingStatus.DECLINED);
        }
    }


    private MeetingResponseDto mapToResponse(Meeting meeting) {

        List<StudentResponseDto> studentDtos = meeting.getStudents()
                .stream()
                .map(this::mapToDto)
                .toList();

        return MeetingResponseDto.builder()
                .id(meeting.getId())
                .sessionColor(meeting.getSessionColor())
                .meetingTitle(meeting.getMeetingTitle())
                .tutorId(meeting.getTutor().getId())
                .scheduledAt(meeting.getScheduledAt())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .meetingType(meeting.getMeetingType())
                .description(meeting.getDescription())
                .students(studentDtos)
                .build();
    }

    private StudentResponseDto mapToDto(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .avatarUrl(student.getAvatarUrl())
                .currentTutorId(student.getTutor() != null ? student.getTutor().getId() : null)
                .assigned(student.getTutor() != null)
                .email(student.getUser().getEmail())
                .build();
    }
}
