package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.CommentDto;
import net.uglevodov.restapi.entities.Comment;
import net.uglevodov.restapi.entities.Image;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.ImageService;
import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = postsService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
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
}
