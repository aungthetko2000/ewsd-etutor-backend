package org.ewsd.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.enumeration.MeetingType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MeetingResponseDto {

    private Long id;
    private String meetingTitle;
    private Long tutorId;
    private List<StudentResponseDto> students;
    private LocalDate scheduledAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private MeetingType meetingType;
    private String description;
    private String location;
    private String virtualPlatform;
    private String virtualPlatformLink;
}
