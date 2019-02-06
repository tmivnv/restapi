package net.uglevodov.restapi.controllers;

import net.uglevodov.restapi.dto.WallDto;
import net.uglevodov.restapi.entities.Post;
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
public class WallsController {

    @Autowired
    WallsService wallsService;

    @GetMapping(value = "/get")
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id,
                                 Pageable pageRequest) {
        var wall = wallsService.get(id);

        return new ResponseEntity<>(new WallDto(wall, pageRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/getByUserId")
    public ResponseEntity<?> getByUserId(@RequestParam(value = "userId") Long userId,
                                         Pageable pageRequest) {
        var wall = wallsService.getByUser(userId);


        return new ResponseEntity<>(new WallDto(wall, pageRequest), HttpStatus.OK);
    }
}
