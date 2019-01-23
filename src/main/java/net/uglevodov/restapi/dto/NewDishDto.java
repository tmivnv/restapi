package net.uglevodov.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewDishDto {

    private String dishName;
    private String description;
    private Long image;
    private int uglevodovnetGroup;
    private Double carbs;
    private Long portion;
    private Boolean active;
    private Set<Long> ingredientsId;
    private String type;
}
