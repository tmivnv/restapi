package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FeedService  {

    Page<Post> findAllByUserId(Long userId, Pageable pageRequest);
    Page<Post> addToFeedByUserId(Long userId, Post post, Pageable pageRequest);
    void addToFeedByUserIds(List<Long> userIds, Post post);


}
