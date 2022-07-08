package com.studentnetwork.userservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name="app_group")
public class AppGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private Long groupID;

    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Post> posts = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinTable(name = "app_user_groups",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<AppUser> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppGroup group = (AppGroup) o;
        return Objects.equals(groupID, group.groupID) && Objects.equals(name, group.name) && Objects.equals(posts, group.posts) && Objects.equals(users, group.users);
    }

    @Override
    public int hashCode() {
        return (int) groupID.intValue() * name.hashCode();
    }
}