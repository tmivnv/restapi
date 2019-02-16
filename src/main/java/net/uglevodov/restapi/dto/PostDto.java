package net.uglevodov.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostDto extends WithId{

    private String text;
    private Set<Long> images;
    private Set<Long> dishes;
    private Long wallId;
    private boolean chatRoomPost;
    private boolean important;
}
