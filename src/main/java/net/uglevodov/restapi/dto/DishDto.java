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

import java.util.Map;
import java.util.Set;

@ApiModel( value = "Dish DTO", description = "Запрос создания/изменения блюда" )
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {

    @ApiModelProperty( value = "Название блюда", required = true )
    private String dishName;
    @ApiModelProperty( value = "Описание блюда", required = true )
    private String description;
    @ApiModelProperty( value = "Айди изображения", required = true )
    private Long image;
    @ApiModelProperty( value = "Путь изображения", required = true )
    private String imagePath;
    @ApiModelProperty( value = "Группа по системе Углеводов.нет", required = true )
    private Integer uglevodovnetGroup;
    @ApiModelProperty( value = "Количество углеводов на 100 гр", required = true )
    private Double carbs;
    @ApiModelProperty( value = "Размер порции", required = true )
    private Long portion;
    @ApiModelProperty( value = "Активно?", required = true )
    private Boolean active;
    @ApiModelProperty( value = "Ингредиенты: айди ингредиента - количество в рецепте", required = true )
    private Set<DishIngredientsDto> ingredients;
    @ApiModelProperty( value = "завтрак/перекус/суп/салат/второе/закуска/десерт", required = true )
    private String type;

}
