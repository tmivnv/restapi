package net.uglevodov.restapi.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes_posts")
public class PostLike extends Owned {

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
}
