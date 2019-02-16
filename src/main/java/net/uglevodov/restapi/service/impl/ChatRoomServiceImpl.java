package net.uglevodov.restapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.uglevodov.restapi.entities.*;
import net.uglevodov.restapi.exceptions.NotFoundException;
import net.uglevodov.restapi.exceptions.NotUpdatableException;
import net.uglevodov.restapi.repositories.ChatRoomRepository;
import net.uglevodov.restapi.repositories.FeedRepository;
import net.uglevodov.restapi.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private FeedRepository feedRepository;


    @Override
    public ChatRoomEntry save(ChatRoomEntry chatRoomEntry) {
        return chatRoomRepository.saveAndFlush(chatRoomEntry);
    }

    @Override
    public ChatRoomEntry get(long id) throws NotFoundException {
        log.trace("[{}] - Getting chatroom entry id = ", this.getClass().getSimpleName(), id);

        return chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("chatroom entry id " + id + " not found"));
    }

    @Override
    public void update(ChatRoomEntry entity) throws NotUpdatableException {

    }

    @Override
    public void delete(long id) throws NotFoundException {

    }

    @Override
    public Page<ChatRoomEntry> getAll(Pageable pageRequest) {
        log.trace("[{}] - Getting wall posts list", this.getClass().getSimpleName());

        return chatRoomRepository.findAll(pageRequest);
    }

    @Override
    public ChatRoomEntry updatePost(Post post) {
        ChatRoomEntry found = chatRoomRepository.findByPost(post).orElseThrow(() -> (new NotFoundException("post not found")));
        found.setPost(post);
        return chatRoomRepository.saveAndFlush(found);
    }

    @Override
    public ChatRoomEntry addPost(Post post) {
        ChatRoomEntry chatRoomEntry = new ChatRoomEntry();
        chatRoomEntry.setPost(post);

        return chatRoomRepository.saveAndFlush(chatRoomEntry);
    }

    @Override
    @Transactional
    public void removePost(Post post) {
        ChatRoomEntry found = chatRoomRepository.findByPost(post).orElseThrow(() -> (new NotFoundException("post not found")));
        found.getPost().setDishSet(null);
        found.getPost().setCommentSet(null);
        found.getPost().setImageSet(null);

        feedRepository.deleteAllByPost(post);

        chatRoomRepository.saveAndFlush(found);
        chatRoomRepository.delete(found);
    }
}
