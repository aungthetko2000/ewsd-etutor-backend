package org.ewsd.repository.message;

import org.ewsd.entity.message.Message;
import org.ewsd.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2)
           OR (m.sender.id = :userId2 AND m.receiver.id = :userId1)
        ORDER BY m.timestamp ASC
    """)
    List<Message> findChatHistory(@Param("userId1") Long userId1,
                                  @Param("userId2") Long userId2);

    @Query("""
        SELECT COUNT(m) FROM Message m
        WHERE m.receiver.id = :receiverId
          AND m.sender.id = :senderId
          AND m.isRead = false
    """)
    long countUnreadMessages(Long receiverId, Long senderId);

    @Modifying
    @Query("""
        UPDATE Message m
        SET m.isRead = true
        WHERE m.receiver.id = :receiverId
          AND m.sender.id = :senderId
          AND m.isRead = false
    """)
    void markMessagesAsRead(@Param("receiverId") Long receiverId,
                            @Param("senderId") Long senderId);

    @Query("""
        SELECT m
        FROM Message m
        WHERE m.id IN (
            SELECT MAX(m2.id)
            FROM Message m2
            WHERE m2.sender.id = :userId OR m2.receiver.id = :userId
            GROUP BY
                CASE
                    WHEN m2.sender.id = :userId THEN m2.receiver.id
                    ELSE m2.sender.id
                END
        )
        ORDER BY m.timestamp DESC
    """)
    List<Message> findLatestConversations(@Param("userId") Long userId);

}
