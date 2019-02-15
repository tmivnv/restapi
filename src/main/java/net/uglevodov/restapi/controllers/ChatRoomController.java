package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/chatroom")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;


    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(chatRoomService.getAll(pageRequest), HttpStatus.OK);
    }
}
