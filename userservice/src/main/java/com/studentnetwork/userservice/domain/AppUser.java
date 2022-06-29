package com.studentnetwork.userservice.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String profilePicturePath = "./resources/images/profile.jpg";
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<AppGroup> groups = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    private Set<Post> postsCreatedByUser;
}