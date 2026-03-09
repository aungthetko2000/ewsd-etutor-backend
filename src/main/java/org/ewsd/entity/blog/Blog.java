package org.ewsd.entity.blog;

import jakarta.persistence.*;
import lombok.*;
import org.ewsd.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    private String imageUrl;

    @JoinColumn(name = "favorite_count")
    private int favoriteCount;

    private boolean likedByCurrentUser;
}