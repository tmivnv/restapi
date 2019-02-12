package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.uglevodov.restapi.dto.SignupDto;
import net.uglevodov.restapi.service.ImageService;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Data
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{


    @Email
    @Column(name = "email", unique = true, updatable = false)
    private String email;

    @NotNull
    @NotBlank
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @NotNull
    @NotBlank
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "avatar")
    private Image avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserInfo> userInfo;


    @Column(name = "is_active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    Set<UserRole> roles;

    public User(Long id,
                @Email String email,
                @NotNull @NotBlank String password,
                Image avatar,
                @NotNull @NotBlank String nickname,
                String firstName,
                String lastName,
                boolean active,
                LocalDateTime created,
                Set<UserRole> roles) {
        super(id);
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.created = created;
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail()) &&
                getNickname().equals(user.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getNickname());
    }
}
