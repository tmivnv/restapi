/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

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
public class IngredientGroupDto {
    private Long id;
    private String name;
    @ApiModelProperty( value = "Ингредиенты: айди ингредиента - количество в рецепте", required = true )
    private Set<DishIngredientsDto> ingredients;
}
