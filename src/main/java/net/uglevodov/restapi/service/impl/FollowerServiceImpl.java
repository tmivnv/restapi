package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Follower;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.FollowerRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    FollowerRepository followerRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void follow(Long followerId, Long followingId) {
        User user = userRepository.findById(followerId).orElseThrow(() -> new NotFoundException("follower not found"));
        userRepository.findById(followingId).orElseThrow(() -> new NotFoundException("user to follow not found"));

        if (user.getFollowing().stream().anyMatch(f->f.getFollowingId().equals(followingId)))
        {
            throw new NotUpdatableException("you are already following this user");
        }


        Follower follower = new Follower();
        follower.setUser(userRepository.getOne(followingId));
        follower.setUserId(followingId);
        follower.setFollowerId(followerId);

        followerRepository.saveAndFlush(follower);
    }

    @Override
    public void unFollow(Long followerId, Long followingId) {
        User user = userRepository.findById(followerId).orElseThrow(() -> new NotFoundException("follower not found"));
        userRepository.findById(followingId).orElseThrow(() -> new NotFoundException("user to unfollow not found"));
        if (user.getFollowing().stream().noneMatch(f->f.getFollowingId().equals(followingId)))
        {
            throw new NotUpdatableException("you are not following this user");
        }

        followerRepository.deleteFollowerByFollowerIdAndUserId(followerId, followingId);
    }
}
