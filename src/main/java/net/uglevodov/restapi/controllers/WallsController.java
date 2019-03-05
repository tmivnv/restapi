/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.*;
import net.uglevodov.restapi.dto.WallDto;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.User;
import net.uglevodov.restapi.entities.Wall;
import net.uglevodov.restapi.service.WallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping(value = "/api/walls")
@Api(value = "/api/walls", description = "Контроллер стен")
public class WallsController {

    @Autowired
    WallsService wallsService;

    @ApiOperation(
            value = "Получить стену по айди СТЕНЫ",
            notes = "Получить стену по айди СТЕНЫ",
            response = WallDto.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
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
    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id,
                                 Pageable pageRequest) {
        var wall = wallsService.get(id);

        return new ResponseEntity<>(new WallDto(wall, pageRequest), HttpStatus.OK);
    }



    @ApiOperation(
            value = "Получить стену по айди ЮЗЕРА",
            notes = "Получить стену по айди ЮЗЕРА",
            response = WallDto.class
    )
    @ApiResponses({

            @io.swagger.annotations.ApiResponse(code = 200, message = "Успех"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Не найден")

    })
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
    @GetMapping(value = "/getByUserId")
    public ResponseEntity<?> getByUserId(@RequestParam(value = "userId") Long userId,
                                         Pageable pageRequest) {
        var wall = wallsService.getByUser(userId);


        return new ResponseEntity<>(new WallDto(wall, pageRequest), HttpStatus.OK);
    }
}
