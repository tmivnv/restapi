package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.Image;

public interface ImageService extends GenericService<Image>{

    Image likeUnlike(Long userId, Long imageId);
}
