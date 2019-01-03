package net.uglevodov.restapi.entities;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate created;

    @Column(name = "image_text")
    private String imageText;



    public Image(User user, @NonNull @URL String imageUrl, String imageText) {
        super(user);
        this.imageUrl = imageUrl;
        this.imageText = imageText;
        this.setCreated(LocalDate.now());
    }
}
