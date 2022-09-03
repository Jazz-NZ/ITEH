package com.studentnetwork.userservice.service;

import com.studentnetwork.userservice.domain.AppUser;
import com.studentnetwork.userservice.domain.AppGroup;
import com.studentnetwork.userservice.domain.Post;
import com.studentnetwork.userservice.domain.Role;

import java.util.List;
import java.util.Map;

public interface AppUserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
    List<Post> getUsersPosts(AppUser user);
    List<AppGroup> getGroups();
    AppGroup saveGroup(AppGroup group);
    Post savePost(Post post);
    void addUserToGroup(String username, String groupName) throws Exception;
    AppGroup getGroup(String groupName);
    void addPostToGroup(Long postID, Long groupID);
    void deleteGroup(String name, String username) throws Exception;
    void addPostByUser(String username, String postDescription);
    List<Role> getRole(String username);
    void updateGroup(String toUpdate, String newName) throws Exception;
    Map<String, Object> getGroupReport(AppGroup group);
    String getCSVReport(String[] groupNames);
    AppUser registerUser(AppUser user) throws Exception;

    void updateUser(String username, String newName) throws Exception;
    void deleteUser(String username) throws Exception;

}
