package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Owned extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="owner_id")
    @JsonIgnore
    @ApiModelProperty( value = "Пользователь")
    private User user;


    @ApiModelProperty( value = "Айди пользователя")
    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long userId;

    public Owned(Long id, User user) {
        super(id);
        this.user = user;

    }

    public Owned(User user) {
        this.user = user;
    }
}