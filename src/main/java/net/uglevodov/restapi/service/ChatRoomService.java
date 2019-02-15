package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.ChatRoomEntry;
import net.uglevodov.restapi.entities.Post;

public interface ChatRoomService extends GenericService<ChatRoomEntry> {
    ChatRoomEntry updatePost(Post post);
    public ChatRoomEntry addPost(Post post);
    public void removePost(Post post);
}
