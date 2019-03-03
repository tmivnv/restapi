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
@ApiModel( value = "Post DTO", description = "Запрос создания/изменения поста" )
public class PostDto{

    @ApiModelProperty( value = "Текст поста", required = true )
    private String text;
    @ApiModelProperty( value = "Массив айди приложенных фотографий (может быть пустым)", required = true )
    private Set<Long> images;
    @ApiModelProperty( value = "Массив айди приложенных блюд (может быть пустым)", required = true )
    private Set<Long> dishes;
    @ApiModelProperty( value = "Айди стены (если пост в разделе общение - не нужен этот параметр)", required = false )
    private Long wallId;
    @ApiModelProperty( value = "Пост в разделе общение?", required = true )
    private boolean chatRoomPost;
    @ApiModelProperty( value = "Пост важный? (Параметр может быть true только для администраторов)", required = true )
    private boolean important;
}
