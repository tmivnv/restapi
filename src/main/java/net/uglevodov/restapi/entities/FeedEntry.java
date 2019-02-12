package net.uglevodov.restapi.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feed")
public class FeedEntry extends Owned {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
