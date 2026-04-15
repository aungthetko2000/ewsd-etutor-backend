package org.ewsd.service.notification;

import org.ewsd.dto.notification.NotificationResponseDto;
import org.ewsd.entity.comment.Comment;
import org.ewsd.entity.meeting.Meeting;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.NotificationType;

import java.util.List;

public interface NotificationService {

    void sendAndSave(User recipient, Meeting meeting, NotificationType type, String message);

    void sendAndSaveComment(User recipient, Comment comment, NotificationType type, String message);

    List<NotificationResponseDto> getNotificationsForUser(String email);

    void markHandled(Long id);

}
