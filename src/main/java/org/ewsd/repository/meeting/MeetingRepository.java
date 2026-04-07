package org.ewsd.repository.meeting;

import org.ewsd.entity.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByTutor_User_Email(String email);

    //View today’s meetings on student dashboard
    @Query("SELECT m FROM Meeting m JOIN m.students s WHERE s.id = :studentId AND m.scheduledAt = :today")
    List<Meeting> findTodayMeetingsByStudent(@Param("studentId") Long studentId,
                                             @Param("today") LocalDate today);
}
