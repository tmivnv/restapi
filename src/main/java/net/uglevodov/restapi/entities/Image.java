package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image extends Owned {

    @NonNull
    @Column(name = "image_url")
    private String imageUrl;

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(name = "image_text")
    private String imageText;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Set<ImageLike> likes;


    public Image(User user, @NonNull @URL String imageUrl, String imageText) {
        super(user);
        this.imageUrl = imageUrl;
        this.imageText = imageText;
        this.setCreated(LocalDateTime.now());
    }
}
