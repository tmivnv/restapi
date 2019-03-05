/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
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
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/posts")
@Api(value = "/api/posts", description = "Контроллер постов")
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

    @Autowired
    UserService userService;

    @ApiOperation(
            value = "Получить пост",
            notes = "Получить пост по айди",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Пост не найден")

    })
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var post = postsService.get(id);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Получить все посты постранично",
            notes = "Получить все посты постранично",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Пост не найден")

    })
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(postsService.getAll(pageRequest), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Поставить/снять лайк посту",
            notes = "Ставит лайк, если нет. Если есть - снимает",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Пост не найден")

    })
    @GetMapping(value = "/like-unlike")
    public ResponseEntity<?> likeUnlike(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return new ResponseEntity<>(postsService.likeUnlike(principal.getId(), id), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Добавить коммент к посту",
            notes = "Добавляет коммент от текущего пользователя",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PostMapping(value = "/add-comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(
            @RequestParam(value = "post_id") Long postId,
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setReplyTo(commentDto.getReplyTo());
        comment.setCreated(LocalDateTime.now());
        comment.setImageSet(commentDto.getImages().stream().map(i -> imageService.get(i)).collect(Collectors.toSet()));

        return new ResponseEntity<>(postsService.addComment(principal.getId(), comment, postId), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Удалить коммент к посту",
            notes = "Удаляет свой коммент (или чужой, если есть права админа)",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @DeleteMapping(value = "/delete-comment")
    public ResponseEntity<?> deleteComment(
            @RequestParam(value = "postId") Long postId,
            @RequestParam(value = "commentId") Long commentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = postsService.get(postId);
        Comment comment = post.getCommentSet().stream().filter(c -> c.getId().equals(commentId)).findFirst().orElse(null);

        return new ResponseEntity<>(postsService.deleteComment(principal.getId(), comment, postId), HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "wallId") Long wallId,
                                    @RequestParam(value = "chatroom") boolean chatRoom,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        if (chatRoom) chatRoomService.removePost(principal.getId(), postsService.get(id));
        else {
            wallsService.removePost(principal.getId(), wallsService.get(wallId), postsService.get(id));
            postsService.delete(id, principal.getId());
        }

        return new ResponseEntity<>(new ApiResponse(true, "post deleted, id " + id), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Сохранить новый пост",
            notes = "Сохранить новый пост",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех")

    })
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = new Post();
        post.setCreated(LocalDateTime.now());
        post.setText(postDto.getText());
        post.setImportant(postDto.isImportant());
        post.setUser(userService.get(principal.getId()));
        post.setImageSet(postDto.getImages().stream().map(i -> imageService.get(i)).collect(Collectors.toSet()));
        post.setDishSet(postDto.getDishes().stream().map(d -> dishesService.get(d)).collect(Collectors.toSet()));

        post = postsService.save(post, principal.getId());

        if (postDto.isChatRoomPost())
            chatRoomService.addPost(post);
        else
            wallsService.addPost(wallsService.get(postDto.getWallId()), post);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Изменить пост",
            notes = "Изменить пост",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestParam(value = "postId") Long postId,
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Post post = postsService.get(postId);
        post.setText(postDto.getText());
        post.setImportant(postDto.isImportant());
        post.setCreated(LocalDateTime.now());
        post.setImageSet(postDto.getImages().stream().map(i -> imageService.get(i)).collect(Collectors.toSet()));
        post.setDishSet(postDto.getDishes().stream().map(d -> dishesService.get(d)).collect(Collectors.toSet()));

        post = postsService.save(post, principal.getId());

        if (postDto.isChatRoomPost())
            chatRoomService.updatePost(principal.getId(), post);
        else
            wallsService.updatePost(principal.getId(), wallsService.get(postDto.getWallId()), post);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
