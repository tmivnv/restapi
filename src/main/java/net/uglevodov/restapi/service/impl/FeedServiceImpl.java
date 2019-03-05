/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.FeedRepository;
import net.uglevodov.restapi.service.FeedService;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    private PostsService postsService;




    @Override
    public Page<Post> findAllByUserId(Long userId, Pageable pageRequest) {
        log.trace("[{}] - Getting feed for user id = ", this.getClass().getSimpleName(), userId);
        List<Post> feed = feedRepository.findAllByUserId(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found")).stream().map(p-> (postsService.postExist(p) ? postsService.get(p) : null)).filter(Objects::nonNull).collect(Collectors.toList());

        return new PageImpl<>(new ArrayList<>(feed), pageRequest, feed.size());
    }

    @Override
    public Page<Post> addToFeedByUserId(Long userId, Post post, Pageable pageRequest) {
        List<Post> feed = feedRepository.addToFeedByUserId(userId, post).orElseThrow(() -> new NotFoundException("user id " + userId + " not found")).stream().map(p-> (postsService.postExist(p) ? postsService.get(p) : null)).filter(Objects::nonNull).collect(Collectors.toList());

        return new PageImpl<>(new ArrayList<>(feed), pageRequest, feed.size());
    }


    @Override
    public void addToFeedByUserIds(List<Long> userIds, Post post) {
        feedRepository.addToFeedByIds(userIds, post);
    }
}
