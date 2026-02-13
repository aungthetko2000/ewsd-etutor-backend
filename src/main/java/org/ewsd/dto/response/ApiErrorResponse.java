package org.ewsd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    @JsonFormat(timezone = "yyyy-MM-dd")
    private LocalDate timeStamp;
    private String code;
    private String message;

}
