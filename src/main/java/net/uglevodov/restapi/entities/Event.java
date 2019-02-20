package net.uglevodov.restapi.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@ApiModel( value = "Событие", description = "Событие, касающееся юзера" )
public class Event extends Owned{



    @ApiModelProperty( value = "Текст события")
    @Column(name = "event_message")
    private String message;

    @ApiModelProperty( value = "Связанная ссылка")
    @Column(name = "event_link")
    private String link;

    @ApiModelProperty( value = "Флаг прочитано/не прочитано")
    @Column(name = "is_read")
    private boolean read;


    @ApiModelProperty( value = "Время события")
    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;
}
