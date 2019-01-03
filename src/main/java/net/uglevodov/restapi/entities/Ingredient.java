package net.uglevodov.restapi.entities;

import lombok.*;

import javax.persistence.*;

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

    @Column(name = "uglevodovnet_group")
    private int uglevodovnetGroup;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_weight")
    private int unitWeight;



}
