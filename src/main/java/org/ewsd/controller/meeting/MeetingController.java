package org.ewsd.controller.meeting;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.meeting.MeetingConfirmationRequest;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.schedule.MeetingRequestDto;
import org.ewsd.dto.schedule.MeetingResponseDto;
import org.ewsd.service.meeting.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') AND hasAuthority('SCHEDULE_MEETING')")
    public ResponseEntity<ApiResponse<MeetingResponseDto>> arrangeMeeting(@RequestBody MeetingRequestDto meetingRequestDto,
                                                                          HttpServletRequest request) {
        MeetingResponseDto meetingResponse = meetingService.createMeeting(meetingRequestDto, request);
        ApiResponse<MeetingResponseDto> response = ApiResponse.success(meetingResponse,
                "Meeting Request Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('TUTOR') AND hasAuthority('VIEW_ALL_SCHEDULE')")
    public ResponseEntity<ApiResponse<List<MeetingResponseDto>>> getAllMeeting(HttpServletRequest request) {
        List<MeetingResponseDto> meetingResponse = meetingService.getAllMeeting(request);
        ApiResponse<List<MeetingResponseDto>> response = ApiResponse.success(meetingResponse,
                "Meeting Request Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/students/emails")
    @PreAuthorize("hasRole('TUTOR') AND hasAuthority('VIEW_STUDENT_EMAIL')")
    public ResponseEntity<ApiResponse<List<String>>> getStudentEmails(@PathVariable Long userId, @RequestParam String email) {
        List<String> emailResponse = meetingService.getAllStudentEmailByTutor(userId, email);
        ApiResponse<List<String>> response = ApiResponse.success(emailResponse, "Email list successfully return");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/status/{meetingId}")
    public ResponseEntity<String> updateMeetingStatus(@PathVariable Long meetingId, @RequestBody MeetingConfirmationRequest request) {
        meetingService.updateMeetingStatus(meetingId, request);
        return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
    }
}
