package com.studentnetwork.userservice;

import com.studentnetwork.userservice.service.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class GroupTests {
    @Autowired
    AppUserService userService;

    @Test
    void saveGroupEmptyParams(){
        Assertions.assertThrows(Exception.class, () -> {userService.saveGroup("","");});
    }

    @Test
    void saveGroupEmptyUsername(){
        Assertions.assertThrows(Exception.class, () -> {userService.saveGroup("randomrandom","");});
    }

    @Test
    void saveGroupEmptyGroupName(){
        Assertions.assertThrows(Exception.class, () -> {userService.saveGroup("","john");});
    }

    @Test
    void deleteGroupEmptyParams(){
        Assertions.assertThrows(Exception.class, () -> {userService.deleteGroup("","");});
    }

    @Test
    void deleteGroupEmptyUsername(){
        Assertions.assertThrows(Exception.class, () -> {userService.deleteGroup("Jovana","");});
    }

    @Test
    void deleteGroupEmptyGroupName(){
        Assertions.assertThrows(Exception.class, () -> {userService.deleteGroup("","john");});
    }
}
