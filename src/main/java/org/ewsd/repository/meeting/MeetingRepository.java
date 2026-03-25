package org.ewsd.repository.meeting;

import org.ewsd.entity.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByTutor_User_Email(String email);
}
