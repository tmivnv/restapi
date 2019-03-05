/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.CommentRepository;
import net.uglevodov.restapi.repositories.EventRepository;
import net.uglevodov.restapi.repositories.PostsRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.EventService;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventService eventService;

    @Override
    public Post save(Post owned, long userId) throws WrongOwnerException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        if (!owned.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN)) throw new NotUpdatableException("This post can not be updated by this user");
        return postsRepository.saveAndFlush(owned);
    }

    @Override
    public Post get(long id) throws NotFoundException {
        log.trace("[{}] - Getting post id = ", this.getClass().getSimpleName(), id);

        return postsRepository.findById(id).orElseThrow(() -> new NotFoundException("post id " + id + " not found"));
    }

    @Override
    public void update(Post owned, long userId) throws NotUpdatableException, WrongOwnerException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));

        if (!owned.getUser().getId().equals(userId)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        postsRepository.saveAndFlush(owned);

    }

    @Override
    public void delete(long id, long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));

        Post post = postsRepository.findById(id).orElseThrow(() -> new NotFoundException("post id " + id + " not found"));

        if (!post.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN))
            throw new WrongOwnerException("This post can not be updated by this user");

        post.setImageSet(null);
        for (Comment comment : post.getCommentSet()) {
            deleteComment(userId, comment, post.getId());
        }
        post.setCommentSet(null);
        post.setDishSet(null);
        postsRepository.saveAndFlush(post);


        postsRepository.delete(post);
    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<Post> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting posts list", this.getClass().getSimpleName());

        return postsRepository.findAll(pageRequest);
    }

    @Override
    public Post likeUnlike(Long userId, Long postId) throws NotFoundException {
        PostLike like = new PostLike();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        like.setUser(user);
        like.setCreated(LocalDateTime.now());
        like.setUserId(userId);

        Post post = postsRepository.findById(postId).orElseThrow(() -> new NotFoundException("post id " + postId + " not found"));
        PostLike alreadyLiked = post.getLikes().stream().filter(l -> l.getUser().getId().equals(userId)).findFirst().orElse(null);

        if (alreadyLiked != null) post.getLikes().remove(alreadyLiked);
        else {
            post.getLikes().add(like);
            //рассказываем фолловерам об этом событии
            for (Follower follower : user.getFollowers()) {

                User followerUser = userRepository.findById(follower.getFollowerId()).orElseThrow(() -> new NotFoundException("user id " + follower.getFollowerId() + " not found"));
                if (followerUser.isFollowingLike()) { //если фолловер хочет видеть этот тип события
                    Event event = new Event();
                    event.setRead(false);
                    event.setCreated(LocalDateTime.now());
                    event.setLink("/api/posts/get?id=" + postId);
                    event.setType("post_like");
                    event.setUser(followerUser);
                    event.setMessage(user.getFirstName() + " " + user.getLastName() + (user.isWoman() ? " оценила" : " оценил") + " пост");
                    if (eventService.findByMessageContainingIgnoreCase(event.getMessage()) == null) //если еще нет такого события
                        eventService.save(event);
                }
            }
        }


        return postsRepository.saveAndFlush(post);
    }

    @Override
    public Post addComment(Long userId, Comment comment, Long postId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        Post post = postsRepository.findById(postId).orElseThrow(() -> new NotFoundException("post id " + postId + " not found"));
        comment.setUser(user);
        comment.setUserId(userId);

        post.getCommentSet().add(comment);

        if (comment.getReplyTo() != null) {
            Event event = new Event();
            event.setRead(false);
            event.setCreated(LocalDateTime.now());
            event.setLink("/api/posts/get?id=" + postId + "#comment=" + comment.getReplyTo());
            event.setType("comment_answer");
            event.setUser(userRepository.findById(
                            commentRepository.findById(
                            comment.getReplyTo()).orElseThrow(() -> new NotFoundException("target comment not found"))
                            .getUserId()).orElseThrow(() -> new NotFoundException("target comment owner not found")));
            event.setMessage(user.getFirstName() + " " + user.getLastName() + (user.isWoman() ? " ответила" : " ответил") + " на ваш комментарий");
            if (eventService.findByMessageContainingIgnoreCase(event.getMessage()) == null) //если еще нет такого события
                eventService.save(event);

        }


        Event event1 = new Event();
        event1.setRead(false);
        event1.setCreated(LocalDateTime.now());
        event1.setLink("/api/posts/get?id=" + postId);
        event1.setType("post_answer");
        event1.setUser(comment.getUser());
        event1.setMessage(user.getFirstName() + " " + user.getLastName() + (user.isWoman() ? " ответила" : " ответил") + " на ваш пост");
        if (eventService.findByMessageContainingIgnoreCase(event1.getMessage()) == null) //если еще нет такого события
            eventService.save(event1);
        return postsRepository.saveAndFlush(post);
    }

    @Override
    public Post deleteComment(Long userId, Comment comment, Long postId) {
        if (comment == null) throw new NotFoundException("comment not found");
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user id " + userId + " not found"));
        Post post = postsRepository.findById(postId).orElseThrow(() -> new NotFoundException("post id " + postId + " not found"));

        if (!comment.getUser().equals(user)&&!user.getRoles().contains(UserRole.ROLE_ADMIN)) //если не автор коммента или не админ - ошибка
            throw new WrongOwnerException("This comment can not be deleted by this user");

        post.getCommentSet().remove(comment);
        comment.setImageSet(null);
        commentRepository.delete(comment);

        return postsRepository.saveAndFlush(post);

    }

    @Override
    public boolean postExist(Long postId) {
        return postsRepository.findById(postId).isPresent();
    }
}
