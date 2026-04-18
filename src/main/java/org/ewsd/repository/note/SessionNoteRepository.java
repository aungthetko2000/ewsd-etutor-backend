package org.ewsd.repository.note;

import org.ewsd.entity.note.SessionNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionNoteRepository extends JpaRepository<SessionNote, Long> {

    Optional<SessionNote> findByMeetingId(Long id);
}
