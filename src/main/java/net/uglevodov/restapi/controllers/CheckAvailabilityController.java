package net.uglevodov.restapi.controllers;

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
public class CheckAvailabilityController {
    private UserService service;

    @Autowired
    public CheckAvailabilityController(UserService service) {
        this.service = service;
    }

    @GetMapping("/check-email-available")
    public ResponseEntity<?> checkEmail(@RequestParam(value = "email") String email) {
        var available = service.checkEmailAvailable(email);
        var response = new ApiResponse(available, available ? "Email available" : "Email already exists");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/check-nickname-available")
    public ResponseEntity<?> checkNickname(@RequestParam(value = "nickname") String nickname) {
        var available = service.checkNicknameAvailable(nickname);
        var response = new ApiResponse(available, available ? "Nickname available" : "Nickname already exists");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}