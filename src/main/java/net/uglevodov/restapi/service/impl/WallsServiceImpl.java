package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.FeedRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.repositories.WallsRepository;
import net.uglevodov.restapi.service.FeedService;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.service.WallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
public class WallsServiceImpl implements WallsService {

    @Autowired
    WallsRepository wallsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedService feedService;

    @Autowired
    UserService userService;

    @Override
    public Wall save(Wall owned, long userId) throws WrongOwnerException {
        owned.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found")));
        return wallsRepository.saveAndFlush(owned);
    }

    @Override
    public Wall get(long id) throws NotFoundException {
        log.trace("[{}] - Getting wall id = ", this.getClass().getSimpleName(), id);

        return wallsRepository.findById(id).orElseThrow(() -> new NotFoundException("wall id " + id + " not found"));
    }

    @Override
    public Wall getByUser(Long userId) throws NotFoundException {
        log.trace("[{}] - Getting wall of user id = ", this.getClass().getSimpleName(), userId);

        return wallsRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
    }

    @Override
    public void update(Wall owned, long userId) throws NotUpdatableException, WrongOwnerException {

    }

    @Override
    public void delete(long id, long userId) throws NotFoundException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

        Wall wall = wallsRepository.findById(id).orElseThrow(() -> new NotFoundException("wall id " + id + " not found"));
        wall.setPosts(null);
        wallsRepository.saveAndFlush(wall);
        wallsRepository.delete(wall);

    }

    @Override
    public Page<Wall> getAll(Pageable pageRequest) {
        return null;
    }

    @Override
    public Wall addPost(Wall wall, Post post) {
        wall.getPosts().add(post);
        feedService.addToFeedByUserIds(post.getUser().getRoles().contains(UserRole.ROLE_ADMIN)&&post.isImportant() ?
                        userService.allUserIds() :
                        post.getUser().getFollowers().stream().map(Follower::getFollowerId).collect(Collectors.toList()),
                post);
        return wallsRepository.saveAndFlush(wall);
    }

    @Override
    @Transactional
    public Wall removePost(Long userId, Wall wall, Post post) {

        User user = userService.get(userId);
        if (!post.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        if (wall.getPosts().contains(post)) wall.getPosts().remove(post);

        return wallsRepository.saveAndFlush(wall);
    }

    @Override
    public Wall updatePost(Long userId, Wall wall, Post post) {

        User user = userService.get(userId);
        if (!post.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        Post found = wall.getPosts().stream().filter(p -> p.getId().equals(post.getId())).findFirst().orElseThrow(() -> (new NotFoundException("post not found on this wall")));
        wall.getPosts().remove(found);
        wall.getPosts().add(post);
        return wallsRepository.saveAndFlush(wall);
    }
}
