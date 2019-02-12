package net.uglevodov.restapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friends")
@JsonIgnoreProperties(value = {"userId", "id"})
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="friend_id")
    @JsonIgnore
    private User user;

    @Column(name = "friend_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "owner_id")
    private Long followerId;
}
