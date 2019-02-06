package net.uglevodov.restapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "walls")
public class Wall extends Owned {
    @Column(name = "is_active")
    private boolean active;


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "wall_posts",
            joinColumns = { @JoinColumn(name = "wall_id") },
            inverseJoinColumns = { @JoinColumn(name = "post_id") }
    )
    Set<Post> posts;
}
