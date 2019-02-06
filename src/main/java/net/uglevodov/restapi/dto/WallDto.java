package net.uglevodov.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.uglevodov.restapi.entities.Post;
import net.uglevodov.restapi.entities.Wall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WallDto extends WithId {

    private Long id;
    private Long userId;
    private boolean active;
    private Page<Post> posts;

    public WallDto(Wall wall, Pageable pageRequest) {
        this(wall.getId(), wall.getUserId(), wall.isActive(), new PageImpl<>(new ArrayList<>(wall.getPosts()), pageRequest, wall.getPosts().size()));
    }
}
