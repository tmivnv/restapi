/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.service.WallsService;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Api(value = "/api/admin/users", description = "Админский User Controller (требуются права Админа)")
public class UserAdminController {
    private UserService userService;

    @Autowired
    private UserUtil utils;

    @Autowired
    private WallsService wallsService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
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
    @PutMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAnother(
            @PathVariable("id") long id,
            @Valid @RequestBody UserUpdateRequestDto updateUserRequest
    ) {
        var user = utils.updateFromUpdateRequest(updateUserRequest, userService.get(id));

        userService.update(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }





    @ApiOperation(
            value = "Изменить пароль другому юзеру",
            notes = "Изменить пароль другому юзеру",
            response = ApiResponse.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/{id}/change-pass")
    public ResponseEntity<?> changeAnotherPassword(
            @RequestParam("pass") String password,
            @PathVariable("id") long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        var user = userService.get(id);

        user.setPassword(password);
        userService.update(user);

        return new ResponseEntity<>(new ApiResponse(
                true,
                String.format("User's id = %d password successfully changed by %s", id, principal.getUsername())),
                HttpStatus.OK
        );
    }






    @ApiOperation(
            value = "Удалить юзера",
            notes = "Удалить юзера",
            response = ApiResponse.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAnother(
            @PathVariable("id") long id) {

        wallsService.delete(wallsService.getByUser(id).getId());
        userService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "User id = " + id + " deleted"), HttpStatus.OK);
    }





    @ApiOperation(
            value = "Изменить статус активности юзера",
            notes = "Изменить статус активности юзера (неактивный - заблокирован)",
            response = ApiResponse.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
    @PutMapping(value = "/active-status")
    public ResponseEntity<?> setActiveStatus(
            @RequestParam(value = "id") long id,
            @RequestParam(value = "active") boolean active
    ) {


        userService.setActive(id, active);

        return new ResponseEntity<>(new ApiResponse(true, "User active status set to " + active), HttpStatus.OK);
    }


}
