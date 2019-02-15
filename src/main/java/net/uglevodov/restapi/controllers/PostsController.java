package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.CommentDto;
import net.uglevodov.restapi.dto.PostDto;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping(value = "/api/posts")
public class PostsController {

    @Autowired
    PostsService postsService;

    @Autowired
    ImageService imageService;

    @Autowired
    DishesService dishesService;

    @Autowired
    WallsService wallsService;

    @Autowired
    ChatRoomService chatRoomService;


    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = postsService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(postsService.getAll(pageRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/like-unlike")
    public ResponseEntity<?> likeUnlike(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    )
    {
        return new ResponseEntity<>(postsService.likeUnlike(principal.getId(), id), HttpStatus.OK);
    }

    @PostMapping(value = "/add-comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(
            @RequestParam(value = "id") Long postId,
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setReplyTo(commentDto.getReplyTo());
        comment.setCreated(LocalDateTime.now());

        Set<Image> imageSet = new HashSet<>();

        for (Long imageId : commentDto.getImages())
        {
            imageSet.add(imageService.get(imageId));
        }

        comment.setImageSet(imageSet);

        return new ResponseEntity<>(postsService.addComment(principal.getId(), comment, postId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "wallId") Long wallId,
                                    @RequestParam(value = "chatroom") boolean chatRoom,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        if (chatRoom) chatRoomService.removePost(postsService.get(id));
        else {
            wallsService.removePost(wallsService.get(wallId), postsService.get(id));
            postsService.delete(id, principal.getId());
        }

        return new ResponseEntity<>(new ApiResponse(true, "post deleted, id "+id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteComment(
            @RequestParam(value = "postId") Long postId,
            @RequestParam(value = "commentId") Long commentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = postsService.get(postId);
        Comment comment = post.getCommentSet().stream().filter(c->c.getId()==commentId).findFirst().orElse(null);

        return new ResponseEntity<>(postsService.deleteComment(principal.getId(), comment, postId), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = new Post();
        post.setCreated(LocalDateTime.now());
        post.setText(postDto.getText());

        Set<Image> images = new HashSet<>();
        for (Long image : postDto.getImages())
        {
            images.add(imageService.get(image));
        }

        post.setImageSet(images);

        Set<Dish> dishes = new HashSet<>();
        for (Long dish : postDto.getDishes())
        {
            dishes.add(dishesService.get(dish));
        }

        post.setDishSet(dishes);

        post = postsService.save(post, principal.getId());

        if (postDto.isChatRoomPost())
            chatRoomService.addPost(post);
        else {
            Wall wall = wallsService.get(postDto.getWallId());
            wallsService.addPost(wall, post);
        }
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestParam(value = "postId") Long postId,
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = postsService.get(postId);
        post.setText(postDto.getText());

        Set<Image> images = new HashSet<>();
        for (Long image : postDto.getImages())
        {
            images.add(imageService.get(image));
        }

        post.setImageSet(images);
        Set<Dish> dishes = new HashSet<>();
        for (Long dish : postDto.getDishes())
        {
            dishes.add(dishesService.get(dish));
        }

        post.setDishSet(dishes);

        post = postsService.save(post, principal.getId());

        if (postDto.isChatRoomPost())
            chatRoomService.updatePost(post);
        else {
            Wall wall = wallsService.get(postDto.getWallId());
            wallsService.updatePost(wall, post);
        }
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }
}
