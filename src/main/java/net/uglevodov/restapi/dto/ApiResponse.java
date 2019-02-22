/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel( value = "API Response", description = "Универсальный ответ API" )
@Data
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
}