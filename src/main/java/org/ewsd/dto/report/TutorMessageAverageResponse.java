package org.ewsd.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorMessageAverageResponse {

    private Long tutorId;
    private String tutorName;
    private Long totalMessages;
    private Long uniqueContacts;
    private Double averageMessagesPerContact;

}
