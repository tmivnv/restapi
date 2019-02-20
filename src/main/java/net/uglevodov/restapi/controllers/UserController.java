package net.uglevodov.restapi.controllers;


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
public class UserController {
    private UserService userService;

    @Autowired
    private UserUtil utils;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowerService followerService;
    @Autowired
    private RedisTemplate<String, Post> redisTemplate;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var user = userService.get(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/follow")
    public ResponseEntity<?> follow(@RequestParam(value = "followId") Long id,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        followerService.follow(principal.getId(), id);
        return new ResponseEntity<>(userService.get(principal.getId()), HttpStatus.OK);
    }

    @GetMapping(value = "/unfollow")
    public ResponseEntity<?> unfollow(@RequestParam(value = "unfollowId") Long id,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        followerService.unFollow(principal.getId(), id);
        return new ResponseEntity<>(userService.get(principal.getId()), HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Valid @RequestBody UserUpdateRequestDto userUpdateRequest,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        var user = utils.updateFromUpdateRequest(userUpdateRequest, userService.get(principal.getId()));
        userService.update(user);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

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


    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(userService.getAll(pageRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/feed")
    public ResponseEntity<?> getFeed(Pageable pageRequest,
                                     @AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(feedService.findAllByUserId(principal.getId(), pageRequest), HttpStatus.OK);
    }


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

    @DeleteMapping(value = "/deleteInfo")
    public ResponseEntity<?> deleteInfo(@RequestParam(value = "infoId") Long infoId,
                                    @AuthenticationPrincipal UserPrincipal principal) {

        userService.removeUserInfo(userService.get(principal.getId()), infoId);
        return new ResponseEntity<>(new ApiResponse(true,"property id " + infoId + " deleted"), HttpStatus.OK);
    }
}