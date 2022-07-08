package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<AppGroup, Long> {
    AppGroup findGroupByName(String name);
    AppGroup findAppGroupByGroupID(Long id);
    @Modifying
    @Query("DELETE FROM AppGroup g WHERE g.name = ?1")
    void deleteByName(String name);
}
