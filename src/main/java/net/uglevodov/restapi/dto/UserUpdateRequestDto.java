package net.uglevodov.restapi.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

@Data
public class UserUpdateRequestDto {

    private Long avatar;

    @Size(min = 3)
    private String firstName;

    @Size(min = 3)
    private String lastName;

    private Boolean isWoman;

    private Boolean isActive;

    private  Boolean isNew;



}