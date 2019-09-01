/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel( value = "Stage DTO", description = "Запрос создания/изменения этапа приготовления блюда" )
public class StageDto {

    private Long stageNumber;
    private String description;
    private Long image;
    private String imagePath;
}
