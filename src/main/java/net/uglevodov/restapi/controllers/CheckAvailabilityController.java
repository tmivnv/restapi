/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@Api( value = "/api/chatroom", description = "Контроллер проверки доступности почты и никнейма" )
public class CheckAvailabilityController {
    private UserService service;

    @Autowired
    public CheckAvailabilityController(UserService service) {
        this.service = service;
    }

    @ApiOperation(
            value = "Проверить занят ли емейл",
            notes = "Проверить занят ли емейл",
            response = ApiResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Свободен, поле success true" ),
            @io.swagger.annotations.ApiResponse( code = 409, message = "Занят" )

    } )
    @GetMapping("/check-email-available")
    public ResponseEntity<?> checkEmail(@RequestParam(value = "email") String email) {
        var available = service.checkEmailAvailable(email);
        var response = new ApiResponse(available, available ? "Email available" : "Email already exists");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Проверить занят ли никнейм",
            notes = "Проверить занят ли никнейм",
            response = ApiResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Свободен, поле success true" ),
            @io.swagger.annotations.ApiResponse( code = 409, message = "Занят" )

    } )
    @GetMapping("/check-nickname-available")
    public ResponseEntity<?> checkNickname(@RequestParam(value = "nickname") String nickname) {
        var available = service.checkNicknameAvailable(nickname);
        var response = new ApiResponse(available, available ? "Nickname available" : "Nickname already exists");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}