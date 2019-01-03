package net.uglevodov.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.uglevodov.restapi.entities.User;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto extends WithId {
    private String nickname;
    private String avatar;
    private String firstName;
    private String lastName;
    private boolean active;
    private LocalDate created;

    public ProfileDto(User user) {
        super(user.getId());
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.active = user.isActive();
        this.created= user.getCreated();
    }
}
