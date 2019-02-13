package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel( value = "Auth Response", description = "Ответ при попытке авторизации" )
@Data
@AllArgsConstructor
public class JwtAuthResponse {
    @ApiModelProperty( value = "JWT Token", required = true )
    private String accessToken;
    @ApiModelProperty( value = "Token type", required = true )
    private String tokenType;
}
