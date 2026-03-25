package org.ewsd.dto.notification;

import lombok.*;
import org.ewsd.enumeration.NotificationType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;
    private String detailMessage;
    private NotificationType type;
    private boolean read;
    private LocalDate scheduledAt;
    private Long meetingId;
    private String senderName;
    private String senderEmail;


}
