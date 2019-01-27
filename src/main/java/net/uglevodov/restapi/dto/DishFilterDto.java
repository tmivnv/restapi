package net.uglevodov.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishFilterDto {

    private List<Long> includeIngredients;
    private List<Long> excludeIngredients;
    private String name;
}
