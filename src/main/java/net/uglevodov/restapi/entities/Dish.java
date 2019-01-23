package net.uglevodov.restapi.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dishes")
public class Dish extends BaseEntity {

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "description")
    private String description;

    @Column(name="image_id")
    private Long image;

    @Column(name = "uglevodovnet_group")
    private int uglevodovnetGroup;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "portion")
    private Long portion;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "dish_id")
    private Set<Recipe> ingredients;

}
