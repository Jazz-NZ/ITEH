package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostByDescription(String desc);
    Post findPostById(Long id);
}
