package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Comment;
import net.uglevodov.restapi.entities.Post;

public interface PostsService extends GenericOwnedService<Post> {

    Post likeUnlike(Long userId, Long postId);
    Post addComment(Long userId, Comment comment, Long postId);
    Post deleteComment(Long userId, Comment comment, Long postId);
    boolean postExist(Long postId);
}
