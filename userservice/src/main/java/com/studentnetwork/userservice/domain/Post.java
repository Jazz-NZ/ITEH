package com.studentnetwork.userservice.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String picturePath = "./resources/images/post.jpg";
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "app_user_id")
//    private AppUser appUser;
}