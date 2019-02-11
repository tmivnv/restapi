package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.ProfileDto;
import net.uglevodov.restapi.dto.UserUpdateRequestDto;
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


    @PutMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAnother(
            @PathVariable("id") long id,
            @Valid @RequestBody UserUpdateRequestDto updateUserRequest
    ) {
        var user = utils.updateFromUpdateRequest(updateUserRequest, userService.get(id));

        userService.update(user);

        return new ResponseEntity<>(new ProfileDto(user), HttpStatus.ACCEPTED);
    }

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
                HttpStatus.ACCEPTED
        );
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAnother(
            @PathVariable("id") long id) {

        wallsService.delete(wallsService.getByUser(id).getId());
        userService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "User id = " + id + " deleted"), HttpStatus.OK);
    }

    @PutMapping(value = "/active-status")
    public ResponseEntity<?> setActiveStatus(
            @RequestParam(value = "id") long id,
            @RequestParam(value = "active") boolean active
    ) {


        userService.setActive(id, active);

        return new ResponseEntity<>(new ApiResponse(true, "User active status set to " + active), HttpStatus.OK);
    }


}
