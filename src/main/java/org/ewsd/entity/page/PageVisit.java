package org.ewsd.entity.page;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.user.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String page;

    private String browser;

    private LocalDateTime visitedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
