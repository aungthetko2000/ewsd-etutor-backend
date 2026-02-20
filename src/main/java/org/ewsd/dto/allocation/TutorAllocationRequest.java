package org.ewsd.dto.allocation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TutorAllocationRequest {

    @NotEmpty(message = "Student IDs cannot be empty")
    private List<Long> studentsId;

    @NotNull(message = "Tutor ID cannot be empty")
    private Long tutorId;

}
