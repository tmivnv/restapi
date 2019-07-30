package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.uglevodov.restapi.dto.IngredientDto;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {


    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "description")
    private String description;

    @Column(name="image_id")
    private Long image;

    @Column(name="image_path")
    private String imagePath;

    @Column(name = "uglevodovnet_group")
    private Integer uglevodovnetGroup;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_weight")
    private Long unitWeight;

    public Ingredient(IngredientDto ingredientDto) {
        this(ingredientDto.getIngredientName(),
                ingredientDto.getDescription(),
                ingredientDto.getImage(),
                ingredientDto.getImagePath(),
                ingredientDto.getUglevodovnetGroup(),
                ingredientDto.getCarbs(),
                ingredientDto.getUnit(),
                ingredientDto.getUnitWeight());

        if (this.unit==null) this.unit = "100 г";
        if (this.unitWeight==null) this.unitWeight = 100L;
    }
}
