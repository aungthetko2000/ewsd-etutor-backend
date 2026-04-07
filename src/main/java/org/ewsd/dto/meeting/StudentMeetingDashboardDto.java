package org.ewsd.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentMeetingDashboardDto {

//    private Long id;
//    private String name;     // tutor name
    private String session;  // meeting title
    private String time;     // "On Going" or "12:30 PM"
    private String status;   // ongoing / upcoming

}