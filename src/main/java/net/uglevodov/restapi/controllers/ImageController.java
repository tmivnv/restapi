/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.UploadFileResponse;
import net.uglevodov.restapi.entities.Image;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.ImageService;
import net.uglevodov.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/images")
@Api( value = "/api/images", description = "Контроллер изображений" )
public class ImageController {

    @Autowired
    private ImageService imageService;


    @ApiOperation(
            value = "Лайк/снятие лайка картинке",
            notes = "Лайк/снятие лайка картинке",
            response = Image.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @GetMapping(value = "/like-unlike")
    public ResponseEntity<?> likeUnlike(
            @RequestParam(value = "id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    )
    {
        return new ResponseEntity<>(imageService.likeUnlike(principal.getId(), id), HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var image = imageService.get(id);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
