package org.ewsd.repository.blog;

import org.ewsd.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findAllByOrderByCreatedAtDesc();

}