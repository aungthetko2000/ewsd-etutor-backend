package org.ewsd.service.report;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.report.MessageLastDaysDto;
import org.ewsd.dto.report.TutorMessageAverageResponse;
import org.ewsd.repository.message.MessageRepository;
import org.ewsd.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    @Override
    public List<TutorMessageAverageResponse> getTutorMessageAverages() {
        return userRepository.getTutorMessageAverages();
    }

    @Override
    public List<MessageLastDaysDto> getMessagesLast7Days() {
        List<Object[]> raw = messageRepository.getMessageCountsLast7Days();
        Map<LocalDate, Long> dbMap = new HashMap<>();
        for (Object[] row : raw) {
            LocalDate date = (LocalDate) row[0];
            Long count = ((Number) row[1]).longValue();
            dbMap.put(date, count);
        }
        List<MessageLastDaysDto> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            result.add(new MessageLastDaysDto(
                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    date,
                    dbMap.getOrDefault(date, 0L)
            ));
        }
        return result;
    }
}
