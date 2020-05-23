/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "heroes")
public class Hero extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "photo_start")
    private String photoStart;

    @Column(name = "photo_finish")
    private String photoFinish;

    @Column(name = "photo_joined")
    private String photoJoined;

    @Column(name = "date_start")
    private LocalDateTime dateStart;

    @Column(name = "date_finish")
    private LocalDateTime dateFinish;

    @Column(name = "weight_start")
    private Double weightStart;

    @Column(name = "weight_finish")
    private Double weightFinish;

    @Column(name = "height")
    private Double height;

    @Column(name = "userId")
    private Long user_id;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "hero_comments",
            joinColumns = { @JoinColumn(name = "hero_id") },
            inverseJoinColumns = { @JoinColumn(name = "comment_id") }
    )
    Set<Comment> commentSet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hero_id")
    private Set<HeroLike> likes;


}
