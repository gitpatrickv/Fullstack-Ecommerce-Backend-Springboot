package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Chat;
import com.practice.fullstackbackendspringboot.entity.Message;
import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.model.ChatModel;
import com.practice.fullstackbackendspringboot.model.response.ChatIdResponse;
import com.practice.fullstackbackendspringboot.repository.ChatRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.ChatService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ChatRepository chatRepository;

    @Override
    public ChatIdResponse createChat(String sender, String recipient) {

        User user = userRepository.findByEmail(sender)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + sender));
        Store store = storeRepository.findById(recipient)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + recipient));
        Boolean isChatExists = chatRepository.existsByStore_StoreIdAndUserEmail(store.getStoreId(),user.getEmail());
        Chat chat;

        if(store.getUser().getEmail().equals(user.getEmail())){
            throw new IllegalArgumentException(StringUtil.SELF_CHAT_NOT_ALLOWED);
        }

        if(!isChatExists) {
            chat = new Chat();
            chat.setUser(user);
            chat.setStore(store);
            chat = chatRepository.save(chat);
        }else {
            chat = chatRepository.findByStore_StoreIdAndUserEmail(store.getStoreId(), user.getEmail()).get();
        }

        ChatIdResponse chatIdResponse = new ChatIdResponse();
        chatIdResponse.setChatId(chat.getChatId());
        return chatIdResponse;
    }

    @Override
    public List<ChatModel> getAllUserChats(String email) {
        List<Chat> chats = chatRepository.findAllByUserEmail(email);
        List<ChatModel> chatModelList = new ArrayList<>();

        for(Chat chat : chats){
            ChatModel chatModel = getChat(chat);
            chatModel.setName(chat.getStore().getStoreName());
            chatModel.setPhotoUrl(chat.getStore().getPhotoUrl());
            chatModelList.add(chatModel);
        }

        return chatModelList;
    }

    @Override
    public List<ChatModel> getAllStoreChats(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        List<ChatModel> chatModelList = new ArrayList<>();
        if(user.get().getRole().equals(Role.SELLER)) {

        Store store = storeRepository.findByUserEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + email));

            List<Chat> chats = chatRepository.findAllByStore_StoreId(store.getStoreId());

            for (Chat chat : chats) {
                ChatModel chatModel = getChat(chat);
                chatModel.setName(chat.getUser().getName());
                chatModel.setPhotoUrl(chat.getUser().getPhotoUrl());
                chatModelList.add(chatModel);
            }
        }

        return chatModelList;
    }


    private ChatModel getChat(Chat chat){
        ChatModel chatModel = new ChatModel();
        chatModel.setChatId(chat.getChatId());
        if(!chat.getMessages().isEmpty()) {
            chat.getMessages().sort(Comparator.comparing(Message::getTimestamp).reversed());
            chatModel.setContent(chat.getMessages().get(0).getContent());
            chatModel.setTimestamp(chat.getMessages().get(0).getTimestamp());
        }
        return chatModel;
    }
}
