/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.CommentDto;
import net.uglevodov.restapi.dto.HeroDto;
import net.uglevodov.restapi.dto.PostDto;
import net.uglevodov.restapi.entities.Comment;
import net.uglevodov.restapi.entities.Hero;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/heroes")
@Api(value = "/api/heroes", description = "Контроллер героев")
public class HeroesController {

    @Autowired
    HeroesService heroesService;

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;

    @ApiOperation(
            value = "Получить героя",
            notes = "Получить пост по айди",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Герой не найден")

    })
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var hero = heroesService.get(id);

        return new ResponseEntity<>(hero, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Получить всех героев постранично",
            notes = "Получить всех героев постранично",
            response = Page.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Пост не найден")

    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(heroesService.getAll(pageRequest), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Поставить/снять лайк герою",
            notes = "Ставит лайк, если нет. Если есть - снимает",
            response = Hero.class
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
        return new ResponseEntity<>(heroesService.likeUnlike(principal.getId(), id), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Добавить коммент к герою",
            notes = "Добавляет коммент от текущего пользователя",
            response = Hero.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PostMapping(value = "/add-comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(
            @RequestParam(value = "hero_id") Long heroId,
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setReplyTo(commentDto.getReplyTo());
        comment.setCreated(LocalDateTime.now());
        comment.setImageSet(commentDto.getImages().stream().map(i -> imageService.get(i)).collect(Collectors.toSet()));

        return new ResponseEntity<>(heroesService.addComment(principal.getId(), comment, heroId), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Удалить коммент к герою",
            notes = "Удаляет свой коммент (или чужой, если есть права админа)",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @DeleteMapping(value = "/delete-comment")
    public ResponseEntity<?> deleteComment(
            @RequestParam(value = "heroId") Long heroId,
            @RequestParam(value = "commentId") Long commentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Hero hero = heroesService.get(heroId);
        Comment comment = hero.getCommentSet().stream().filter(c -> c.getId().equals(commentId)).findFirst().orElse(null);

        return new ResponseEntity<>(heroesService.deleteComment(principal.getId(), comment, heroId), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Удалить героя",
            notes = "Удаляет героя (если есть права админа)",
            response = ApiResponse.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден"),
            @io.swagger.annotations.ApiResponse(code = 409, message = "Не может быть удален этим юзером")

    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") @ApiParam(value = "post id", example = "4100003", required = true) Long id ) {


            heroesService.delete(id);


        return new ResponseEntity<>(new ApiResponse(true, "hero deleted, id " + id), HttpStatus.OK);
    }


    @ApiOperation(
            value = "Сохранить нового героя",
            notes = "Сохранить нового героя",
            response = Post.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех")

    })
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(
            @RequestBody HeroDto heroDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Hero hero = new Hero();
        hero.setName(heroDto.getName());
        hero.setHeight(heroDto.getHeight());
        hero.setDateStart(heroDto.getDateStart());
        hero.setDateFinish(heroDto.getDateFinish());
        hero.setPhotoStart(heroDto.getPhotoStart());
        hero.setPhotoFinish(heroDto.getPhotoFinish());
        hero.setUser_id(heroDto.getUser_id());
        hero.setWeightStart(heroDto.getWeightStart());
        hero.setWeightFinish(heroDto.getWeightFinish());

        hero = heroesService.save(hero);


        return new ResponseEntity<>(hero, HttpStatus.OK);
    }


}
