package org.ewsd.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.enumeration.MeetingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingConfirmationRequest {

    private MeetingStatus meetingStatus;
    private String reason;

}
