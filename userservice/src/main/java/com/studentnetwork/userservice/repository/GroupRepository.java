package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<AppGroup, Long> {
    AppGroup findGroupByName(String name);
    AppGroup findAppGroupById(Long id);
    void deleteById(Long id);
}
