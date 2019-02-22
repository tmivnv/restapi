/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.entities.ChatRoomEntry;
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
@Api( value = "/api/chatroom", description = "Контроллер секции ОБЩЕНИЕ" )
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @ApiOperation(
            value = "Получить все записи раздела общение",
            notes = "Получить все записи раздела общение, постранично, запрос может содержать Pageable request",
            response = ChatRoomEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(chatRoomService.getAll(pageRequest), HttpStatus.OK);
    }
}
