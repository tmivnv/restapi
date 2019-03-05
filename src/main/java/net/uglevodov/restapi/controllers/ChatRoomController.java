/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import net.uglevodov.restapi.entities.ChatRoomEntry;
import net.uglevodov.restapi.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            response = Page.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageRequest) {
        return new ResponseEntity<>(chatRoomService.getAll(pageRequest), HttpStatus.OK);
    }
}
