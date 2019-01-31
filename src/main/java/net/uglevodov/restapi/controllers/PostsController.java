package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/posts")
public class PostsController {

    @Autowired
    PostsService postsService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var dish = postsService.get(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);
    }
}
