package org.ewsd.service.report;

import org.ewsd.dto.report.MessageLastDaysDto;
import org.ewsd.dto.report.TutorMessageAverageResponse;

import java.util.List;

public interface StatisticsService {

    List<TutorMessageAverageResponse> getTutorMessageAverages();

    List<MessageLastDaysDto> getMessagesLast7Days();

}
