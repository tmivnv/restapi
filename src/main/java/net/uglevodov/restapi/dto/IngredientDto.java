/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel( value = "DTO: Ингредиент", description = "Ингредиент, из которых состоят блюда" )
@Data
@AllArgsConstructor
public class IngredientDto {

    @ApiModelProperty( value = "Название")
    private String ingredientName;
    @ApiModelProperty( value = "Описание")
    private String description;
    @ApiModelProperty( value = "Айди картинки")
    private Long image;
    @ApiModelProperty( value = "Группа по системе Углеводов нет")
    private int uglevodovnetGroup;
    @ApiModelProperty( value = "Углеводов на 100 гр")
    private Double carbs;
    @ApiModelProperty( value = "Название единицы измерения (по умолчанию 100 г)")
    private String unit;
    @ApiModelProperty( value = "Вес единицы измерения в граммах (по умолчанию 100)")
    private Long unitWeight;
}
