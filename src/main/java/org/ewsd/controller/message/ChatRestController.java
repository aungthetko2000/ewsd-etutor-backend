package org.ewsd.controller.message;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.message.ChatContactResponse;
import org.ewsd.dto.message.ChatMessageResponse;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.service.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final MessageService messageService;

    @GetMapping("/history")
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('GET_CHAT_HISTORY')")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatHistory(@RequestParam Long userId1, @RequestParam Long userId2) {
        List<ChatMessageResponse> chatMessageResponses = messageService.getChatHistory(userId1, userId2);
        ApiResponse<List<ChatMessageResponse>> response = ApiResponse.success(chatMessageResponses, "Getting chat history");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/read")
    public ResponseEntity<Void> markAsRead(@RequestParam Long receiverId, @RequestParam Long senderId) {
        messageService.markAsRead(receiverId, senderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam Long receiverId, @RequestParam Long senderId) {
        long count = messageService.countUnread(receiverId, senderId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @GetMapping("/contacts")
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('GET_CHAT_CONTACTS')")
    public ResponseEntity<ApiResponse<List<ChatContactResponse>>> getChatContacts(@RequestParam Long userId) {
       List<ChatContactResponse> userResponse = messageService.getChatContacts(userId);
       ApiResponse<List<ChatContactResponse>> response = ApiResponse.success(userResponse, "Get latest chat contact");
       return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/partner")
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('VIEW_ALL_STUDENTS')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getStudentList(@RequestParam String name) {
        List<StudentResponseDto> studentResponse = messageService.getAllStudents(name);
        ApiResponse<List<StudentResponseDto>> response = ApiResponse.success(studentResponse, "Get latest chat contact");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
