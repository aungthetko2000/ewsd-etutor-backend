package org.ewsd.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.message.Message;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    private String senderEmail;
    private String receiverEmail;

    public static ChatMessageResponse from(Message msg) {
        ChatMessageResponse res = new ChatMessageResponse();
        res.id         = msg.getId();
        res.senderId   = msg.getSender().getId();
        res.senderName = msg.getSender().getFirstName() + msg.getSender().getLastName();
        res.receiverId = msg.getReceiver().getId();
        res.content    = msg.getContent();
        res.timestamp  = msg.getTimestamp();
        res.isRead     = msg.isRead();
        res.senderEmail = msg.getSender().getEmail();
        res.receiverEmail = msg.getReceiver().getEmail();
        return res;
    }

}
