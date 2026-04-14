package org.ewsd.service.staff;

import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.student.StudentResponseDto;

import java.util.List;

public interface StaffService {

    List<TutorAllocationResponse> bulkAllocateStudentsToTutor(List<Long> studentIds, Long tutorId);

    List<StudentResponseDto> getStudentsWithNoTutor();

}
