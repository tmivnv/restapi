package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Post;

import java.util.List;
import java.util.Optional;

public interface FeedRepository  {
    Optional<List<Post>> findAllByUserId(Long userId);
    Optional<List<Post>> addToFeedByUserId(Long userId, Post post);
    void addToFeedByIds(List<Long> userIds, Post post);
    void deleteAllByPost(Post post);
}
