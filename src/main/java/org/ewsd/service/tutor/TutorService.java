package org.ewsd.service.tutor;

import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.entity.user.User;

import java.util.List;

public interface TutorService {

    List<TutorResponse> getAllTutors();

    List<StudentResponseDto> getAssignedStudents(String email);

}