package com.studentnetwork.userservice.repository;

import com.studentnetwork.userservice.domain.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<AppGroup, Long> {
    AppGroup findGroupByName(String name);
    AppGroup findAppGroupByGroupID(Long id);
    @Modifying
    @Query("DELETE FROM AppGroup g WHERE g.name = ?1")
    void deleteByName(String name);

    @Modifying
    @Query("update AppGroup g set g.name = ?1 where g.groupID = ?2")
    void setGroupNameByID(String groupName, Long id);

    @Query(value = "SELECT COUNT(ug.user_id) FROM app_group g JOIN app_user_groups ug ON ug.group_id = g.group_id WHERE g.name LIKE :groupname",
            nativeQuery = true)
    String getUserCount(@Param("groupname") String groupname);

    AppGroup getAppGroupByName(String name);
}
