package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Follower;

public interface FollowerService {

    void follow(Long followerId, Long followingId);
    void unFollow(Long followerId, Long followingId);
}
