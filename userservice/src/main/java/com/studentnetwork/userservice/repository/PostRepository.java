package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostByDescription(String desc);
    Post findPostById(Long id);

    @Modifying
    @Query("update Post p set p.title = ?1,p.description=?2 where p.id = ?3")
    void setTitleAndDescriptionByID(String title, String description, Long id);
}
