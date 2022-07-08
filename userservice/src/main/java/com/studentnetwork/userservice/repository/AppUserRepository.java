package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.AppUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
    AppUser findAppUserByUserID(Long id);
}
