/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.EventDto;
import net.uglevodov.restapi.dto.JwtAuthResponse;
import net.uglevodov.restapi.entities.Event;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.EventService;
import net.uglevodov.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/events")
@Api( value = "/api/events", description = "Контроллер событий" )

public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "Добавить событие (Требуются права админа)",
            notes = "Ручное добавление события",
            response = Event.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody EventDto eventDto
            ) {
        Event event = new Event();
        event.setCreated(LocalDateTime.now());
        event.setLink(eventDto.getLink());
        event.setMessage(eventDto.getMessage());
        event.setRead(eventDto.isRead());
        event.setUser(userService.get(eventDto.getUserId()));


        var saved = eventService.save(event);
        return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
    }


    @ApiOperation(
            value = "Получить событие (Требуются права админа)",
            notes = "Просмотр события по айди",
            response = Event.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Айди не найден" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var event = eventService.get(id);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Получить список событий по айди юзера (Требуются права админа)",
            notes = "Получить список событий по айди юзера",
            response = Event.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Айди не найден" )

    } )
    @GetMapping(value = "/getByUserId")
    public ResponseEntity<?> getAllByUserId(Pageable pageRequest, @RequestParam(value = "user_id") Long userId) {
        return new ResponseEntity<>(eventService.getAllByUserId(userId, pageRequest), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Получить список моих событий",
            notes = "Получить список моих событий",
            response = Event.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @GetMapping(value = "/getMyEvents")
    public ResponseEntity<?> getMyEvents(Pageable pageRequest,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(eventService.getAllByUserId(principal.getId(), pageRequest), HttpStatus.OK);
    }
}
