package org.ewsd.service.message;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.message.ChatMessageRequest;
import org.ewsd.dto.message.ChatMessageResponse;
import org.ewsd.entity.message.Message;
import org.ewsd.entity.user.User;
import org.ewsd.repository.message.MessageRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        User sender = userRepository.findByEmail(request.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .content(request.getContent())
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .build();

        Message saved = messageRepository.save(message);
        return ChatMessageResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(Long userId1, Long userId2) {
        return messageRepository.findChatHistory(userId1, userId2)
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long receiverId, Long senderId) {
        messageRepository.markMessagesAsRead(receiverId, senderId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long receiverId, Long senderId) {
        return messageRepository.countUnreadMessages(receiverId, senderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getChatContacts(Long userId) {
        return messageRepository.findChatContacts(userId);
    }
}
