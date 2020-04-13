/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.uglevodov.restapi.entities.Dish;

import java.util.Set;

@ApiModel( value = "Dish Category DTO", description = "Категорияи блюд" )
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishCategoryDto {

    private Long id;
    @ApiModelProperty( value = "Название категории", required = true )
    private String categoryName;
    @ApiModelProperty( value = "Количество блюд", required = true )
    private Long dishesNumber;
    @ApiModelProperty( value = "Сет блюд", required = true )
    private Set<Dish> dishes;

}
