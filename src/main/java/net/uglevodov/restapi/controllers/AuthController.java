package net.uglevodov.restapi.controllers;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.dto.JwtAuthResponse;
import net.uglevodov.restapi.dto.LoginDto;
import net.uglevodov.restapi.dto.SignupDto;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.security.JwtTokenProvider;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private JwtTokenProvider tokenProvider;
    private UserService service;
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserUtil utils;

    @Autowired
    public AuthController(JwtTokenProvider tokenProvider, UserService service, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.service = service;
        this.authenticationManager = authenticationManager;
    }

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

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@Valid @RequestBody SignupDto signupRequest) {
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

            return ResponseEntity.created(location).body(new ApiResponse(true, "User successfully registered!"));

    }
}