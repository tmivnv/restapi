package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends Owned {

    @Column(name = "text")
    private String text;

    @NonNull
    @Column(name = "created", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(name = "important")
    private boolean important;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "post_images",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") }
    )
    Set<Image> imageSet;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "post_dishes",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "dish_id") }
    )
    Set<Dish> dishSet;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "post_comments",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "comment_id") }
    )
    Set<Comment> commentSet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Set<PostLike> likes;
}
