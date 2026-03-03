package org.ewsd.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.enumeration.MeetingStatus;
import org.ewsd.enumeration.MeetingType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequestDto {

    private String meetingTitle;
    private LocalDate scheduledAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private String tutorEmail;
    private List<String> studentEmail = new ArrayList<>();
    private MeetingType meetingType;
    private String sessionColor;
    private String description;
    private String location;
    private String virtualPlatform;
    private String virtualPlatformLink;

}
