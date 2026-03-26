package org.ewsd.service.notification.impl;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.notification.NotificationResponseDto;
import org.ewsd.entity.Notification;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.NotificationStatus;
import org.ewsd.enumeration.NotificationType;
import org.ewsd.repository.notification.NotificationRepository;
import org.ewsd.service.notification.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendAndSave(User recipient, Meeting meeting, NotificationType type, String message) {
        Notification notification = Notification.builder()
                .scheduledAt(meeting.getScheduledAt())
                .recipient(recipient)
                .meeting(meeting)
                .type(type)
                .message(message)
                .status(NotificationStatus.UNREAD)
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        NotificationResponseDto dto = mapToDto(saved);

        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(),
                "/queue/notifications",
                dto
        );
    }

    @Override
    public List<NotificationResponseDto> getNotificationsForUser(String email) {
        return notificationRepository.findByRecipientEmailAndStatusOrderByScheduledAtDesc(email, NotificationStatus.UNREAD)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markHandled(Long id) {
        Notification n = notificationRepository.findById(id).orElseThrow();
        n.setStatus(NotificationStatus.HANDLED);
        n.setRead(true);
    }

    public NotificationResponseDto mapToDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .senderEmail(notification.getMeeting().getTutor().getUser().getEmail())
                .senderName(notification.getMeeting().getTutor().getFullName())
                .detailMessage(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .scheduledAt(notification.getScheduledAt())
                .meetingId(notification.getMeeting() != null ? notification.getMeeting().getId() : null)
                .build();
    }
}
