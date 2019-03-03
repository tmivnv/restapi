/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel( value = "Comment DTO", description = "Запрос создания/изменения комментария к посту" )
public class CommentDto {

    @ApiModelProperty( value = "Текст комментария", required = true )
    private String text;
    @ApiModelProperty( value = "Айди КОММЕНТАРИЯ(!) на который отвечаем", required = false )
    private Long replyTo;
    @ApiModelProperty( value = "Массив айди приложенных фотографий (может быть пустым)", required = true )
    private Set<Long> images;
}
