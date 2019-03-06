/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.entities.StaticPage;
import net.uglevodov.restapi.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/pages")
@Api(value = "/api/pages", description = "Контроллер статичных страниц")
public class StaticPagesController {



    @Autowired
    private StaticPageService staticPageService;


    @GetMapping(value = "/get")
    @ApiOperation(
            value = "Получить страницу по айди",
            notes = "Получить страницу по айди",
            response = StaticPage.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Не найдена" )

    } )
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var page = staticPageService.get(id);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
