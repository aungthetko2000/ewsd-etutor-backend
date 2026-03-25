package org.ewsd.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.message.Message;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatContactResponse {

    private Long partnerId;
    private String partnerName;
    private String lastMessage;
    private LocalDateTime timestamp;
    private String partnerFirstName;
    private String partnerLastName;
    private String partnerEmail;
    private long unreadCount;

    public static ChatContactResponse from(Message msg, Long currentUserId) {

        ChatContactResponse res = new ChatContactResponse();

        if (msg.getSender().getId().equals(currentUserId)) {
            res.partnerFirstName = msg.getReceiver().getFirstName();
            res.partnerLastName = msg.getReceiver().getLastName();
            res.partnerId = msg.getReceiver().getId();
            res.partnerName = msg.getReceiver().getFirstName() + " " + msg.getReceiver().getLastName();
            res.partnerEmail = msg.getReceiver().getEmail();
        } else {
            res.partnerFirstName = msg.getSender().getFirstName();
            res.partnerLastName = msg.getSender().getLastName();
            res.partnerId = msg.getSender().getId();
            res.partnerName = msg.getSender().getFirstName() + " " + msg.getSender().getLastName();
            res.partnerEmail = msg.getSender().getEmail();
        }

        res.lastMessage = msg.getContent();
        res.timestamp = msg.getTimestamp();

        return res;
    }
}
