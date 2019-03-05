/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.ChatRoomRepository;
import net.uglevodov.restapi.service.ChatRoomService;
import net.uglevodov.restapi.service.FeedService;
import net.uglevodov.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private FeedService feedService;


    @Autowired
    private UserService userService;


    @Override
    public ChatRoomEntry save(ChatRoomEntry chatRoomEntry) {
        return chatRoomRepository.saveAndFlush(chatRoomEntry);
    }

    @Override
    public ChatRoomEntry get(long id) throws NotFoundException {
        log.trace("[{}] - Getting chatroom entry id = ", this.getClass().getSimpleName(), id);

        return chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("chatroom entry id " + id + " not found"));
    }

    @Override
    public void update(ChatRoomEntry entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<ChatRoomEntry> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting wall posts list", this.getClass().getSimpleName());

        return chatRoomRepository.findAll(pageRequest);
    }

    @Override
    public ChatRoomEntry updatePost(Long userId, Post post) {

        User user = userService.get(userId);
        if (!post.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        ChatRoomEntry found = chatRoomRepository.findByPost(post).orElseThrow(() -> (new NotFoundException("post not found")));


        found.setPost(post);
        return chatRoomRepository.saveAndFlush(found);
    }

    @Override
    public ChatRoomEntry addPost(Post post) {
        ChatRoomEntry chatRoomEntry = new ChatRoomEntry();
        chatRoomEntry.setPost(post);

        feedService.addToFeedByUserIds(post.getUser().getRoles().contains(UserRole.ROLE_ADMIN)&&post.isImportant() ?
                        userService.allUserIds() :
                        post.getUser().getFollowers().stream().map(Follower::getFollowerId).collect(Collectors.toList()),
                        post);

        return chatRoomRepository.saveAndFlush(chatRoomEntry);

    }

    @Override
    @Transactional
    public void removePost(Long userId, Post post) {

        User user = userService.get(userId);
        if (!post.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        ChatRoomEntry found = chatRoomRepository.findByPost(post).orElseThrow(() -> (new NotFoundException("post not found")));
        found.getPost().setDishSet(null);
        found.getPost().setCommentSet(null);
        found.getPost().setImageSet(null);


        chatRoomRepository.saveAndFlush(found);
        chatRoomRepository.delete(found);
    }
}
