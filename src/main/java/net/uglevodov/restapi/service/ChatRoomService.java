package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.ChatRoomEntry;
import net.uglevodov.restapi.entities.Post;

public interface ChatRoomService extends GenericService<ChatRoomEntry> {
    ChatRoomEntry updatePost(Long userId, Post post);
    ChatRoomEntry addPost(Post post);
    void removePost(Long userID, Post post);
}
