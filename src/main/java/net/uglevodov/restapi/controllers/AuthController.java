/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.JwtAuthResponse;
import net.uglevodov.restapi.dto.LoginDto;
import net.uglevodov.restapi.dto.SignupDto;
import net.uglevodov.restapi.entities.Wall;
import net.uglevodov.restapi.security.JwtTokenProvider;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.service.WallsService;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
@Api( value = "/api/auth", description = "Авторизация и регистрация нового пользователя" )
public class AuthController {
    private JwtTokenProvider tokenProvider;
    private UserService service;
    private AuthenticationManager authenticationManager;


    @Autowired
    WallsService wallsService;

    @Autowired
    private UserUtil utils;

    @Autowired
    public AuthController(JwtTokenProvider tokenProvider, UserService service, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.service = service;
        this.authenticationManager = authenticationManager;
    }


    @ApiOperation(
            value = "Авторизация",
            notes = "Выдает JWT токен",
            response = JwtAuthResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 401, message = "Неверный логин/пароль" ),
            @io.swagger.annotations.ApiResponse( code = 200, message = "Все в порядке, выдается JWT токен" )

    } )
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody
                                            LoginDto loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getAuthName(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(jwt,"Bearer"));
    }

    @ApiOperation(
            value = "Регистрация нового пользователя",
            notes = "Создает нового пользователя",
            response = ApiResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 400, message = "Ошибка валидации DTO" ),
            @io.swagger.annotations.ApiResponse( code = 200, message = "Все в порядке, пользователь зарегистрирован" )

    } )
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupRequest) {
         if (!service.checkEmailAvailable(signupRequest.getEmail())) {
                return new ResponseEntity<>(new ApiResponse(false, "Email already exists!"), HttpStatus.BAD_REQUEST);
            }

            if (!service.checkNicknameAvailable(signupRequest.getNickname())) {
                return new ResponseEntity<>(new ApiResponse(false, "Nickname already exists!"), HttpStatus.BAD_REQUEST);
            }

            var user = utils.signUpFromSignUpDto(signupRequest);
            var location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("users/{nickname}")
                    .buildAndExpand(service.save(user).getNickname())
                    .toUri();

            Wall wall = new Wall();
            wall.setUser(user);
            wall.setActive(true);
            wallsService.save(wall, user.getId());

            return ResponseEntity.created(location).body(new ApiResponse(true, "User successfully registered!"));

    }

}