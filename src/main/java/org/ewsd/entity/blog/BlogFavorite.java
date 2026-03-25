package org.ewsd.entity.blog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ewsd.entity.user.User;

@Entity
@Table(name = "blog_favorites")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Blog blog;

}
