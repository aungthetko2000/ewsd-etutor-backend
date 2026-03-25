package org.ewsd.repository.blogfavorite;

import org.ewsd.entity.blog.BlogFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogFavoriteRepository extends JpaRepository<BlogFavorite, Long> {

    Optional<BlogFavorite> findByUserEmailAndBlogId(String userEmail, Long blogId);

    int countByBlogId(Long blogId);

    boolean existsByUserEmailAndBlogId(String email, Long blogId);
}
