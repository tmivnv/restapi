package net.uglevodov.restapi.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel( value = "DTO: Событие", description = "Событие, касающееся юзера" )
@Data
@AllArgsConstructor
public class EventDto {

    @ApiModelProperty( value = "Айди пользователя")
    private Long userId;

    @ApiModelProperty( value = "Текст события")
    private String message;

    @ApiModelProperty( value = "Связанная ссылка")
    private String link;

    @ApiModelProperty( value = "Тип события")
    private String type;

    @ApiModelProperty( value = "Флаг прочитано/не прочитано")
    private boolean read;
}
