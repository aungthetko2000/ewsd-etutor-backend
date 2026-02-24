package org.ewsd.service.staff;

import org.ewsd.dto.allocation.TutorAllocationResponse;

import java.util.List;

public interface StaffService {

    List<TutorAllocationResponse> bulkAllocateStudentsToTutor(List<Long> studentIds, Long tutorId);

}
