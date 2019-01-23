package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

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

    @Column(name = "uglevodovnet_group")
    private int uglevodovnetGroup;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_weight")
    private Long unitWeight;



}
