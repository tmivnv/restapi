package net.uglevodov.restapi.controllers;


import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.utils.PasswordUtil;
import net.uglevodov.restapi.utils.PrivilegeUtil;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var user = userService.get(id);

        return new ResponseEntity<>(new ProfileDto(user), HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Valid @RequestBody UserUpdateRequestDto userUpdateRequest,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        var user = UserUtil.updateFromUpdateRequest(userUpdateRequest, userService.get(principal.getId()));
        userService.update(user);

        return new ResponseEntity<>(new ProfileDto(user), HttpStatus.ACCEPTED);
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
        //return new ResponseEntity<>(UserUtil.usersToProfiles(userService.getAll(pageRequest)), HttpStatus.OK);
        return new ResponseEntity<>(userService.getAll(pageRequest), HttpStatus.OK);
    }
}