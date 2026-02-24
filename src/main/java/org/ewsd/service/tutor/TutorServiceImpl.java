package org.ewsd.service.tutor;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.repository.tutor.TutorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;

    @Override
    public List<TutorResponse> getAllTutors() {
        return tutorRepository.findAll().stream()
                .map(tutor -> TutorResponse.builder()
                        .id(tutor.getId())
                        .fullName(tutor.getFullName())
                        .email(tutor.getUser().getEmail())
                        .build())
                .toList();
    }
}