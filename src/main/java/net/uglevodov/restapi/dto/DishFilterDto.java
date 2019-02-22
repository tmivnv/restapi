package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel( value = "Dish filter DTO", description = "Запрос поиска блюд" )
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishFilterDto {

    @ApiModelProperty( value = "Список айди включаемых ингридиентов", required = true )
    private List<Long> includeIngredients;
    @ApiModelProperty( value = "Список айди исключаемых ингридиентов", required = true )
    private List<Long> excludeIngredients;
    @ApiModelProperty( value = "Содержит в названии", required = true )
    private String name;
}
