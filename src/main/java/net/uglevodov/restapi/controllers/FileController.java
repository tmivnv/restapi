/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.UploadFileResponse;
import net.uglevodov.restapi.entities.Event;
import net.uglevodov.restapi.entities.Image;
import net.uglevodov.restapi.security.UserPrincipal;
import net.uglevodov.restapi.service.ImageService;
import net.uglevodov.restapi.service.UserService;
import net.uglevodov.restapi.service.impl.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/files")
@Api( value = "/api/files", description = "Контроллер файлов/изображений" )
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "Загрузка файла",
            notes = "Загрузка файла, занесение его в таблицу Images",
            response = UploadFileResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("text") String text, @AuthenticationPrincipal UserPrincipal principal) {
        String fileName = fileStorageService.storeFile(file);

        Image image = new Image(userService.get(principal.getId()), fileName, text);

        image = imageService.save(image);



        return new UploadFileResponse(fileName, image.getId(), image.getImageUrl(),
                file.getContentType(), file.getSize());
    }

    @ApiOperation(
            value = "Получить файл по имени",
            notes = "Получить файл по имени"

    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ApiOperation(
            value = "Получить файл по его айди",
            notes = "Получить файл по его айди"

    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @GetMapping("/downloadFileId/{id}")
    public ResponseEntity<Resource> downloadFileId(@PathVariable Long id, HttpServletRequest request) {

        return downloadFile(imageService.get(id).getImageUrl(), request);



    }
}