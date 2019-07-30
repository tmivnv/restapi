/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;


import io.swagger.annotations.*;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.UserInfoDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.UserInfo;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.repositories.FeedRepository;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.FeedService;
import net.uglevodov.restapi.service.FollowerService;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.utils.PasswordUtil;
import net.uglevodov.restapi.utils.PrivilegeUtil;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/users")
@Api(value = "/api/users", description = "Контроллер юзеров")
public class UserController {
    private UserService userService;

    @Autowired
    private UserUtil utils;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowerService followerService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @ApiOperation(
            value = "Получить юзера",
            notes = "Получить юзера",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var user = userService.get(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }





    @ApiOperation(
            value = "Подписаться на обновления юзера",
            notes = "Подписаться на обновления юзера",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @GetMapping(value = "/follow")
    public ResponseEntity<?> follow(@RequestParam(value = "followId") Long id,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        followerService.follow(principal.getId(), id);
        return new ResponseEntity<>(userService.get(principal.getId()), HttpStatus.OK);
    }






    @ApiOperation(
            value = "Отписаться от обновлений юзера",
            notes = "Отписаться от обновлений юзера",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @GetMapping(value = "/unfollow")
    public ResponseEntity<?> unfollow(@RequestParam(value = "unfollowId") Long id,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        followerService.unFollow(principal.getId(), id);
        return new ResponseEntity<>(userService.get(principal.getId()), HttpStatus.OK);
    }





    @ApiOperation(
            value = "Изменить юзера",
            notes = "Изменить юзера",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Valid @RequestBody UserUpdateRequestDto userUpdateRequest,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        var user = utils.updateFromUpdateRequest(userUpdateRequest, userService.get(principal.getId()));
        userService.update(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @ApiOperation(
            value = "Сменить свой пароль",
            notes = "Сменить свой пароль",
            response = ApiResponse.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "Не верный старый пароль"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/change-pass")
    public ResponseEntity<?> changePassword(
            @RequestParam(value = "old") String oldPassword,
            @RequestParam(value = "new") String newPassword,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        if (!PasswordUtil.isMatching(oldPassword, principal.getPassword())) {
            return new ResponseEntity<>(new ApiResponse
                    (false, "Old password does not correspond to the current one"),
                    HttpStatus.FORBIDDEN);
        }

        var user = userService.get(principal.getId());
        user.setPassword(newPassword);
        userService.update(user);

        return new ResponseEntity<>(new ApiResponse(true, "Password successfully changed"), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Получить всех юзеров",
            notes = "Получить всех юзеров",
            response = Page.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех")

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
        return new ResponseEntity<>(userService.getAll(pageRequest), HttpStatus.OK);
    }







    @ApiOperation(
            value = "Получить свою ленту новостей",
            notes = "Получить свою ленту новостей",
            response = Page.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех")

    })
    @GetMapping(value = "/feed")
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
    public ResponseEntity<?> getFeed(Pageable pageRequest,
                                     @AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(feedService.findAllByUserId(principal.getId(), pageRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/getme")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        var user = userService.get(principal.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Добавить информацию о юзере",
            notes = "Добавить информацию о юзере",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PostMapping(value = "/addInfo")
    public ResponseEntity<?> addUserInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserInfoDto userInfoDto
            )
    {
        UserInfo userInfo = new UserInfo();
        userInfo.setProp(userInfoDto.getProp());
        userInfo.setPropValue(userInfoDto.getPropValue());
        return new ResponseEntity<>(userService.addUserInfo(userService.get(principal.getId()), userInfo), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Изменить информацию о юзере",
            notes = "Изменить информацию о юзере",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/updateInfo")
    public ResponseEntity<?> updateUserInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserInfoDto userInfoDto
    )
    {
        UserInfo userInfo = new UserInfo();
        userInfo.setProp(userInfoDto.getProp());
        userInfo.setPropValue(userInfoDto.getPropValue());
        return new ResponseEntity<>(userService.updateUserInfo(userService.get(principal.getId()), userInfo), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Удалить информацию о юзере",
            notes = "Удалить информацию о юзере",
            response = User.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @DeleteMapping(value = "/deleteInfo")
    public ResponseEntity<?> deleteInfo(@RequestParam(value = "infoId") Long infoId,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        userService.removeUserInfo(userService.get(principal.getId()), infoId);
        return new ResponseEntity<>(new ApiResponse(true,"property id " + infoId + " deleted"), HttpStatus.OK);
    }
}