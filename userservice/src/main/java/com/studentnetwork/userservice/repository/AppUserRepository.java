package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.AppUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
    AppUser findAppUserByUserID(Long id);

    @Modifying
    @Query("update AppUser u set u.name = ?1 where u.username = ?2")
    void setNameByUsername(String name, String username);

    @Modifying
    @Query("DELETE FROM AppUser u WHERE u.username = ?1")
    void deleteByUsername(String username);
}
