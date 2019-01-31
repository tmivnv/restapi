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
@Table(name = "comments")
public class Comment extends Owned {

    @Column(name = "text")
    private String text;

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(name = "reply_to")
    private Long replyTo;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "comment_images",
            joinColumns = { @JoinColumn(name = "comment_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") }
    )
    Set<Image> imageSet;
}
