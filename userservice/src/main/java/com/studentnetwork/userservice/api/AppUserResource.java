package com.studentnetwork.userservice.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentnetwork.userservice.domain.AppUser;
import com.studentnetwork.userservice.domain.AppGroup;
import com.studentnetwork.userservice.domain.Post;
import com.studentnetwork.userservice.domain.Role;
import com.studentnetwork.userservice.exceptions.ErrorBodyException;
import com.studentnetwork.userservice.service.AppUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AppUserResource {
    private final AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>>getAppUsers(){
        return ResponseEntity.ok().body(userService.getAppUsers());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AppUser> getAppUser(@PathVariable String username){
        return ResponseEntity.ok().body(userService.getAppUser(username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveAppUsers(@RequestBody AppUser user){
        log.info("Trying to save new user: {}",user.getUsername());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    @GetMapping("/admin/groups")
    public ResponseEntity<List<AppGroup>> getGroups(){
        return ResponseEntity.ok().body(userService.getGroups());
    }

    @PostMapping("/admin/group/report")
    public ResponseEntity<Map<String, Object>> getGroup(@RequestBody AppGroup group){
        log.info("Trying to get report for group: {}", group.getName());
        return ResponseEntity.ok().body(userService.getGroupReport(group));
    }

    @PostMapping("/admin/group/update")
    public ResponseEntity<?> updateGroupName(@RequestBody Map<String, Object> payload){
        String toUpdate = (String)payload.get("toUpdate");
        String newName = (String)payload.get("newName");
        log.info("Trying to update group {} to {}", toUpdate, newName);
        try {
            userService.updateGroup(toUpdate,newName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ErrorBodyException(e.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/group/save", consumes={"application/json"})
    public ResponseEntity saveGroup(@RequestBody Map<String, Object> payload){
        String username = (String)payload.get("username");
        String groupname = (String)payload.get("groupname");
        log.info("USERNAME {}", username);
        log.info("Trying to save new group: {}", groupname);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/group/save").toUriString());
        try {
            userService.saveGroup(groupname, username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:"+e.getMessage().toString());
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping(value="/group/add", consumes={"application/json"})
    public ResponseEntity addUserToGroup(@RequestBody Map<String, Object> payload){
        String username = (String)payload.get("username");
        String groupname = (String)payload.get("groupname");
        log.info("Trying to add user {} to  group: {}", username,groupname);
        try {
            userService.addUserToGroup(username, groupname);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:"+e.getMessage().toString());
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/post/save")
    public ResponseEntity<Post> savePost(@RequestBody PostByUser postByUser){
        log.info("Trying to save new post {} by user {}", postByUser.getPost().getDescription(), postByUser.getUsername());
        AppUser user = userService.getAppUser(postByUser.getUsername());
        AppGroup group = userService.getGroup(postByUser.getGroupname());
        if(!user.getGroups().contains(group)) {
            return ResponseEntity.badRequest().build(); //case that user is not member of group
        }
        Post post = postByUser.getPost();
        post.setGroupName(postByUser.getGroupname());
        //post.setAppUser(user);
        post = userService.savePost(post);
        userService.addPostToGroup(post.getId(),group.getGroupID());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(@RequestBody AppUser user){
        return ResponseEntity.ok().body(userService.getUsersPosts(user));
    }
    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserFrom form ){
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/role" )
    public ResponseEntity<List<Role>> getUserRole(@RequestBody AppUser user ){
        return ResponseEntity.ok().body(userService.getRole(user.getUsername()));
    }

    @DeleteMapping("/group/{groupName}/{username}")
    public ResponseEntity delete(@PathVariable String groupName,@PathVariable String username){
        try {
            userService.deleteGroup(groupName,username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:"+e.getMessage().toString());
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> image(@RequestBody Map<String, Object> payload){
        String path =(String) payload.get("path");

        final ByteArrayResource inputStream;
        try {
            inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            log.error("Error on getting picture");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
        log.info("Picture returned successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @PostMapping("/admin/report")
    public ResponseEntity<Map<String,String>> getCSVReportForGroup(@RequestBody String[] groupNames){
        System.out.println(groupNames[0]);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("data",userService.getCSVReport(groupNames));
        return ResponseEntity.ok().body(responseMap);
    }

    @PostMapping("/user/update")
    public ResponseEntity<AppUser> updateUserName(@RequestBody Map<Object, String> payload){
        //String userIDD = payload.get("userID");
        String username = payload.get("username");
        String name = payload.get("newName");
        //Long userID = Long.parseLong(userIDD);
        log.info("Trying to update user {} to {}", username, name);
        System.out.println("OVO JE TO UPDATE: "+ username);
        try {
            userService.updateUser(username,name);
        } catch (Exception e) {
            log.error("Error on updating user name");
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/delete/{username}")
    public ResponseEntity deleteUser(@PathVariable String username){
        try {
            log.info("Trying to delete user {}", username);

            userService.deleteUser(username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:"+e.getMessage().toString());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/update")
    public ResponseEntity<AppUser> updatePost(@RequestBody Map<Object, String> payload){
        String postIDD = (String) payload.get("postID");
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        Long postID = Long.parseLong(postIDD);
        log.info("Trying to update post {}", postID);
        try {
            userService.updatePost(postID,title,description);
        } catch (Exception e) {
            log.error("Error on updating post");
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/delete/{postID}")
    public ResponseEntity deletePost(@PathVariable Long postID){
        try {
            log.info("Trying to delete post {}", postID);

            userService.deletePost(postID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:"+e.getMessage().toString());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerAppUser(@RequestBody AppUser user) throws Exception {
        log.info("Trying to register new user: {}",user.getUsername());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/register").toUriString());
        return ResponseEntity.created(uri).body(userService.registerUser(user));
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getAppUser(username);
                String refreshToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                //response.setHeader("access_token", accessToken);
                //response.setHeader("refresh_token", refreshToken);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                log.error("Error geting new access token in: {}",exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }

    }
}

@Data
class RoleToUserFrom{
    private String username;
    private String roleName;
}
@Data
class UserToGroup{
    private String username;
    private String groupName;
}
@Data
class PostByUser{
    private Post post;
    private String username;
    private String groupname;
}