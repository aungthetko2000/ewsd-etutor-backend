package org.ewsd.repository.blog;

import org.ewsd.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findAllByOrderByCreatedAtDesc();

    List<Blog> findTop6ByOrderByFavoriteCountDesc();

    @Query("""
    SELECT b
    FROM Blog b
    JOIN FETCH b.user
    WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(CAST(b.content as string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY b.createdAt DESC
""")
    List<Blog> searchBlogs(@Param("keyword") String keyword);
}