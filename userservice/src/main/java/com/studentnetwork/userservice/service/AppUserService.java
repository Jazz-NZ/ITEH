package com.studentnetwork.userservice.service;

import com.studentnetwork.userservice.domain.AppUser;
import com.studentnetwork.userservice.domain.AppGroup;
import com.studentnetwork.userservice.domain.Post;
import com.studentnetwork.userservice.domain.Role;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
    List<Post> getPosts();
    List<AppGroup> getGroups();
    AppGroup saveGroup(AppGroup group);
    Post savePost(Post post);
    void addUserToGroup(String username, String groupName) throws Exception;
    AppGroup getGroup(String groupName);
    void addPostToGroup(Long postID, Long groupID);
    void deleteGroup(String name, String username);
    void addPostByUser(String username, String postDescription);
}
