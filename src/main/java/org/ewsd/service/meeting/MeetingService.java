package org.ewsd.service.meeting;

import jakarta.servlet.http.HttpServletRequest;
import org.ewsd.dto.meeting.MeetingConfirmationRequest;
import org.ewsd.dto.meeting.StudentMeetingDashboardDto;
import org.ewsd.dto.schedule.MeetingRequestDto;
import org.ewsd.dto.schedule.MeetingResponseDto;

import java.util.List;

public interface MeetingService {

    MeetingResponseDto createMeeting(MeetingRequestDto meetingRequestDto, HttpServletRequest request);

    List<MeetingResponseDto> getAllMeeting(HttpServletRequest request);

    List<String> getAllStudentEmailByTutor(Long tutorId, String email);

    void updateMeetingStatus(Long id, MeetingConfirmationRequest request);

    //View today’s meetings on student dashboard
    List<StudentMeetingDashboardDto> getTodayMeetingsForStudent(HttpServletRequest request);
}
