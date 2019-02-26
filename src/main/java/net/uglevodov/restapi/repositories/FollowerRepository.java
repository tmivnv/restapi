package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    @Transactional
    @Modifying
    @Query("delete from Follower where followerId = :followerId and userId = :userId")
    void deleteFollowerByFollowerIdAndUserId(Long followerId, Long userId);
}
