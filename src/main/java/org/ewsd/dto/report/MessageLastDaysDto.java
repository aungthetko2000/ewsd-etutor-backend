package org.ewsd.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageLastDaysDto {

    private String day;
    private LocalDate date;
    private Long count;

}
