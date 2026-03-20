package org.ewsd.service.tutor;

import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import java.util.List;

public interface TutorService {

    List<TutorResponse> getAllTutors();
    List<StudentResponseDto> getAssignedStudents(Long tutorId);

}