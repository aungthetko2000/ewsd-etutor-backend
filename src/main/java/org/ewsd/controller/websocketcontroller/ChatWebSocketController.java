package org.ewsd.controller.websocketcontroller;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.message.ChatMessageRequest;
import org.ewsd.dto.message.ChatMessageResponse;
import org.ewsd.service.message.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request) {
        ChatMessageResponse response = messageService.saveMessage(request);
        System.out.println("Received message: " + request.getSenderEmail());
        messagingTemplate.convertAndSendToUser(
                request.getReceiverEmail(),
                "/queue/messages",
                response
        );

        System.out.println("Sending to user: " + request.getReceiverEmail());
        messagingTemplate.convertAndSendToUser(
                request.getSenderEmail(),
                "/queue/messages",
                response
        );
    }
}
