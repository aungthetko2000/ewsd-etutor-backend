package org.ewsd.service.message;

import org.ewsd.dto.message.ChatContactResponse;
import org.ewsd.dto.message.ChatMessageRequest;
import org.ewsd.dto.message.ChatMessageResponse;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.user.User;

import java.util.List;

public interface MessageService {

    ChatMessageResponse saveMessage(ChatMessageRequest request);

    List<ChatMessageResponse> getChatHistory(Long userId1, Long userId2);

    void markAsRead(Long receiverId, Long senderId);

    long countUnread(Long receiverId, Long senderId);

    List<ChatContactResponse> getChatContacts(Long userId);

    List<StudentResponseDto> getAllStudents(String name);

}
