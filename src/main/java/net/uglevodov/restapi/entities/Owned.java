package net.uglevodov.restapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Owned extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User user;

    public Owned(Long id, User user) {
        super(id);
        this.user = user;
    }
}