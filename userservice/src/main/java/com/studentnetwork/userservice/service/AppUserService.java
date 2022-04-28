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
    AppGroup saveGroup(AppGroup group);
    Post savePost(Post post);
    void addUserToGroup(String username, String groupName);
    void addPostToGroup(String postDescription, String groupName);
    void addPostByUser(String username, String postDescription);
}
