package org.ewsd.repository.notification;

import org.ewsd.entity.Notification;
import org.ewsd.enumeration.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Fetch all notifications for a user, newest first
    List<Notification> findByRecipientEmailAndStatusOrderByScheduledAtDesc(String email, NotificationStatus status);

}
