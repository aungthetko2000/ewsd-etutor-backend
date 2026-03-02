package org.ewsd.repository.blog;

import org.ewsd.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}