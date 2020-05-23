/*
 * Copyright (c) 2020. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel( value = "Hero DTO", description = "Запрос создания/изменения героя" )
public class HeroDto {


    private String name;

    private String photoStart;

    private String photoFinish;

    private String photoJoined;

    private LocalDateTime dateStart;

    private LocalDateTime dateFinish;

    private Double weightStart;

    private Double weightFinish;

    private Double height;

    private Long user_id;

}
