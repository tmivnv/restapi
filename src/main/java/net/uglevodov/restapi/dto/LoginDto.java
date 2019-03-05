package net.uglevodov.restapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@ApiModel( value = "Login data transfer object", description = "Запрос при логине" )
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NonNull
    @Setter
    @Getter
    @ApiModelProperty( value = "Login", required = true, example = "test@gmail.com")
    private String authName;
    @NonNull
    @Setter
    @Getter
    @ApiModelProperty( value = "Password", required = true, example = "qwerty")
    private String password;
}