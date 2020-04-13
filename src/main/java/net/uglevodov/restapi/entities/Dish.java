/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.uglevodov.restapi.dto.DishDto;
import net.uglevodov.restapi.dto.NewDishDto;
import net.uglevodov.restapi.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
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

    @Column(name="image_path")
    private String imagePath;

    @Column(name = "uglevodovnet_group")
    private int uglevodovnetGroup;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "portion")
    private Long portion;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dish_id")
    private Set<IngredientGroup> ingredientGroups;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dish_id")
    private Set<CookingStages> stages;

    @Column(name = "type")
    private String type;

    public void setTypeNumber(Integer typeNumber) {
        String[] types =  {"Завтраки","Супы","Салаты","Закуски","Десерты","Запеканки и омлеты","Второе","Гарниры","Перекусы"};
        this.typeNumber = typeNumber;
        this.type = types[typeNumber-1];
    }

    @Column(name = "typeNumber")
    private Integer typeNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dish_id")
    private Set<FavoredByUser> favoredByUsers;


}
