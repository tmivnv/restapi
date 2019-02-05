package net.uglevodov.restapi.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes_images")
public class ImageLike extends Owned {

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;


}
