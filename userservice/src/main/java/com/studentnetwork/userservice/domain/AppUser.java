package com.studentnetwork.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name="app_user")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userID;
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER,cascade = { CascadeType.ALL })
    @JsonIgnoreProperties("users")
    @JoinTable(name = "app_user_groups",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    )
    private Set<AppGroup> groups = new HashSet<>();
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    //private Set<Post> postsCreatedByUser;

    @Override
    public int hashCode() {
        return (int) userID.intValue() * username.hashCode();
    }
}