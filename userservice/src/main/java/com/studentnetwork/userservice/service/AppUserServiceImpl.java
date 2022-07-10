package com.studentnetwork.userservice.service;

import com.studentnetwork.userservice.domain.AppUser;
import com.studentnetwork.userservice.domain.AppGroup;
import com.studentnetwork.userservice.domain.Post;
import com.studentnetwork.userservice.domain.Role;
import com.studentnetwork.userservice.repository.AppUserRepository;
import com.studentnetwork.userservice.repository.GroupRepository;
import com.studentnetwork.userservice.repository.PostRepository;
import com.studentnetwork.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service  @RequiredArgsConstructor @Transactional @Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository userRepo;
    private final RoleRepository roleRepo;
    private final GroupRepository groupRepo;
    private final PostRepository postRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName() );
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        AppUser user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public AppUser getAppUser(String username) {
        log.info("Fetching user {}",username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getAppUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
    @Override
    public List<Post> getUsersPosts(AppUser user) {
        log.info("Fetching posts for user");
        Set<AppGroup> groups = userRepo.findByUsername(user.getUsername()).getGroups();
        List<Post> posts = new ArrayList<>();
        for(AppGroup g : groups){
            posts.addAll(g.getPosts());
        }
        return posts;
    }

    @Override
    public List<AppGroup> getGroups() {
        log.info("Fetching all groups");
        return groupRepo.findAll();
    }

    @Override
    public AppGroup saveGroup(AppGroup group) {
        log.info("Saving new group {} to the database", group.getName());
        return groupRepo.save(group);
    }

    @Override
    public Post savePost(Post post) {
        log.info("Saving new post {} to the database", post.getDescription());
        return postRepo.save(post);
    }

    @Override
    public void addUserToGroup(String username, String groupName) throws Exception {
        log.info("Adding user {} to groupName {}", username, groupName);
        AppUser user = userRepo.findByUsername(username);
        if(Objects.isNull(user)){
            log.error("There is no user found");
            throw new Exception("Error on passing user");}
        AppGroup group = groupRepo.findGroupByName(groupName);
        if(Objects.isNull(group)){
            log.error("There is no group found");
            throw new Exception("Error on finding group");}
        for(AppGroup g : user.getGroups()){
            if(g.getName().equalsIgnoreCase(groupName)){
                log.info("User is already member of group");
                throw new Exception("User is already member of this group");}
        }
        user.getGroups().add(group);
    }

    @Override
    public AppGroup getGroup(String groupName) {
        log.info("Getting group by group name: {}",groupName);
        return groupRepo.findGroupByName(groupName);
    }

    @Override
    public void deleteGroup(String groupName, String username) throws Exception {
        log.info("Deleting group by group name: {}",groupName);
        AppGroup group = groupRepo.findGroupByName(groupName);
        if(Objects.isNull(group)){
            throw new Exception("Error cant find group");
        }
        group.getUsers().clear();
        group.getPosts().clear();
        //AppUser appUser=userRepo.findByUsername(username);
        //appUser.getGroups().removeIf(x-> x.getName().equalsIgnoreCase(groupName));
        //groupRepo.delete(group);
        groupRepo.deleteByName(groupName);
        groupRepo.flush();

    }
    @Override
    public void addPostToGroup(Long postID, Long groupID) {
        log.info("Adding postDescription {} to groupName {}", postID, groupID);
        Post post = postRepo.findPostById(postID);
        AppGroup group = groupRepo.findAppGroupByGroupID(groupID);
        group.getPosts().add(post);
        //user.getRoles().add(role);
    }

    @Override
    public void addPostByUser(String username, String postDescription) {
        log.info("This shouldn't be implemented ");
    }

    @Override
    public List<Role> getRole(String username) {
        log.info("Getting role for user {}", username);
        AppUser user = userRepo.findByUsername(username);
        return (List)user.getRoles();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if(user==null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in database");
        }else{
            log.info("User found in the database: {}",username);
        }
        Collection<SimpleGrantedAuthority> authoithies = new ArrayList<>();
        user.getRoles().forEach(role -> {authoithies.add(new SimpleGrantedAuthority(role.getName()));});

        return new User(user.getUsername(),user.getPassword(),authoithies);
    }
}
