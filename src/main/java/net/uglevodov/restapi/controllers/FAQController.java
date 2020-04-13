/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import net.uglevodov.restapi.dto.ApiResponse;
import net.uglevodov.restapi.entities.FAQEntry;
import net.uglevodov.restapi.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/faq")
@Api(value = "/api/faq", description = "Контроллер частых вопросов")
public class FAQController {

    @Autowired
    private FAQService faqService;


    @GetMapping(value = "/get")
    @ApiOperation(
            value = "Получить ответ по айди",
            notes = "Получить ответ по айди",
            response = FAQEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Не найден" )

    } )
    public ResponseEntity<?> get(@RequestParam(value = "id") Long id) {
        var answer = faqService.get(id);

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(faqService.getAll(Pageable.unpaged()).getContent(), HttpStatus.OK);
    }




    @GetMapping(value = "/getbycategory")
    @ApiOperation(
            value = "Получить ответ по айди категории",
            notes = "Получить ответ по айди категории",
            response = FAQEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Не найден" )

    } )
    public ResponseEntity<?> getByCategory(@RequestParam(value = "id") Integer id) {
        var answer = faqService.findAllByCategory(id);

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }



    @ApiOperation(
            value = "Поиск вопросов",
            notes = "Получить вопросы по тексту вопросов/ответов (список вопросов, в тексте которых есть искомая строка, case insensitive)",
            response = FAQEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Не найдены" )

    } )
    @GetMapping(value = "find")
    public ResponseEntity<?> findByName(@RequestParam(value = "string") String string) {
        return new ResponseEntity<>(faqService.findAllByQuestionOrAnswerContaining(string), HttpStatus.OK);
    }




    @ApiOperation(
            value = "Добавить новый вопрос (требуются права админа)",
            notes = "Добавить новый вопрос (требуются права админа)",
            response = FAQEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            @RequestBody FAQEntry faqEntry
    ) {

        return new ResponseEntity<>(faqService.save(faqEntry), HttpStatus.OK);
    }






    @ApiOperation(
            value = "Изменить вопрос (требуются права админа)",
            notes = "Изменить вопрос (требуются права админа)",
            response = FAQEntry.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Вопрос не найдены" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestBody FAQEntry faqEntry
    ) {
        FAQEntry found = faqService.get(faqEntry.getId());
        found.setCategory(faqEntry.getCategory());
        found.setQuestion(faqEntry.getQuestion());
        found.setAnswer(faqEntry.getAnswer());
        faqService.update(found);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }







    @ApiOperation(
            value = "Удалить вопрос (требуются права админа)",
            notes = "Удалить вопрос (требуются права админа)",
            response = ApiResponse.class
    )
    @ApiResponses( {

            @io.swagger.annotations.ApiResponse( code = 200, message = "Успех" ),
            @io.swagger.annotations.ApiResponse( code = 404, message = "Вопрос не найден" )

    } )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        faqService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "FAQ entry deleted, id "+id), HttpStatus.OK);
    }
}
