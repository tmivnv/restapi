/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "ingredient_group")
public class IngredientGroup extends BaseEntity {


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "group_id")
    private Set<Recipe> group;

    @Column(name="name")
    private String name;




}
