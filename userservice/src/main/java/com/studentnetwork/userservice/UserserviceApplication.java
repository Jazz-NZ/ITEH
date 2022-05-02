package com.studentnetwork.userservice;

import com.studentnetwork.userservice.domain.AppGroup;
import com.studentnetwork.userservice.domain.AppUser;
import com.studentnetwork.userservice.domain.Post;
import com.studentnetwork.userservice.domain.Role;
import com.studentnetwork.userservice.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(AppUserService userService){
//		return args -> {
//			userService.saveRole(new Role(null,"ROLE_USER"));
//			userService.saveRole(new Role(null,"ROLE_MANAGER"));
//			userService.saveRole(new Role(null,"ROLE_ADMIN"));
//			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
//
//			userService.saveUser(new AppUser(null,"John Travolta","john","1234",new ArrayList<>(),new ArrayList<>(),null));
//			userService.saveUser(new AppUser(null,"Will Smith","will","1234",new ArrayList<>(),new ArrayList<>(),new HashSet<>()));
//			userService.saveUser(new AppUser(null,"Jim Carry","jim","1234",new ArrayList<>(),new ArrayList<>(),new HashSet<>()));
//			userService.saveUser(new AppUser(null,"Leonardo Di Caprio","leo","1234",new ArrayList<>(),new ArrayList<>(), new HashSet<>()));
//
//			userService.addRoleToUser("john","ROLE_USER");
//			userService.addRoleToUser("john","ROLE_MANAGER");
//			userService.addRoleToUser("jim","ROLE_USER");
//			userService.addRoleToUser("will","ROLE_SUPER_ADMIN");
//			userService.addRoleToUser("leo","ROLE_ADMIN");
//
//			userService.savePost(new Post(null,"Novi title","Novi desc", userService.getAppUser("john")));
//			userService.saveGroup(new AppGroup(null,"Grupa"));
//			userService.addUserToGroup("john","Grupa");
//		};
//	}
}
