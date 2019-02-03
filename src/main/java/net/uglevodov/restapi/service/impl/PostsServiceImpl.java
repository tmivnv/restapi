package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.Comment;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.PostLike;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.exceptions.WrongOwnerException;
import net.uglevodov.restapi.repositories.PostsRepository;
import net.uglevodov.restapi.repositories.UserRepository;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostsServiceImpl implements PostsService {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Post save(Post owned, long userId) throws WrongOwnerException {
        return null;
    }

    @Override
    public Post get(long id) throws NotFoundException {
        log.trace("[{}] - Getting post id = ", this.getClass().getSimpleName(), id);

        return postsRepository.findById(id).orElseThrow(() -> new NotFoundException("post id " + id + " not found"));
    }

    @Override
    public void update(Post owned, long userId) throws NotUpdatableException, WrongOwnerException {

    }

    @Override
    public void delete(long id, long userId) throws NotFoundException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public List<Post> getAll(long userId) throws NotFoundException {
        return null;
    }

    @Override
    public Post likeUnlike(Long userId, Long postId) throws NotFoundException {
        PostLike like = new PostLike();
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("user id " + userId + " not found"));
        like.setUser(user);

        Post post = postsRepository.findById(postId).orElseThrow(()-> new NotFoundException("post id " + postId + " not found"));
        PostLike alreadyLiked = post.getLikes().stream().filter(l -> l.getUser().getId()==userId).findFirst().orElse(null);

        if (alreadyLiked!=null) post.getLikes().remove(alreadyLiked);
        else
            post.getLikes().add(like);

        return postsRepository.saveAndFlush(post);
    }

    @Override
    public Post addComment(Long userId, Comment comment, Long postId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("user id " + userId + " not found"));
        Post post = postsRepository.findById(postId).orElseThrow(()-> new NotFoundException("post id " + postId + " not found"));
        comment.setUser(user);

        post.getCommentSet().add(comment);

        return postsRepository.saveAndFlush(post);
    }

    @Override
    public Post deleteComment(Long userId, Comment comment, Long postId) {
        if (comment==null) throw new NotFoundException("comment not found");
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("user id " + userId + " not found"));
        Post post = postsRepository.findById(postId).orElseThrow(()-> new NotFoundException("post id " + postId + " not found"));

        if (!comment.getUser().equals(user)) throw new WrongOwnerException("This comment can not be deleted by this user");

        post.getCommentSet().remove(comment);

        return postsRepository.saveAndFlush(post);

    }
}
