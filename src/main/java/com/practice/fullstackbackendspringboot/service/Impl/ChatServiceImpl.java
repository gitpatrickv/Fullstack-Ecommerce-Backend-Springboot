package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Chat;
import com.practice.fullstackbackendspringboot.entity.Message;
import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.ChatModel;
import com.practice.fullstackbackendspringboot.model.MessageModel;
import com.practice.fullstackbackendspringboot.repository.ChatRepository;
import com.practice.fullstackbackendspringboot.repository.MessageRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.ChatService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public void createChat(String sender, String recipient) {

        User user = userRepository.findByEmail(sender)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + sender));
        Store store = storeRepository.findById(recipient)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + recipient));
        Boolean isChatExists = chatRepository.existsByStore_StoreIdAndUserEmail(store.getStoreId(),user.getEmail());

        if(!isChatExists) {
            Chat chat = new Chat();
            chat.setUser(user);
            chat.setStore(store);
            chatRepository.save(chat);
        }
    }

    @Override
    public ChatModel getChatById(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);

        if(chatOptional.isPresent()){
            Chat chat = chatOptional.get();
            ChatModel chatModel = getChat(chat);

            List<Message> messages = messageRepository.findAllByChat_ChatId(chatId);
            List<MessageModel> messageModelList = new ArrayList<>();

            for(Message message : messages) {
                MessageModel messageModel = messageMapper.mapEntityToModel(message);
                messageModel.setChatId(chat.getChatId());
                messageModelList.add(messageModel);
            }
            chatModel.setMessageModelList(messageModelList);

            return chatModel;
        }
        else {
            throw new NoSuchElementException(StringUtil.CHAT_NOT_FOUND + chatId);
        }

    }

    @Override
    public List<ChatModel> getAllUserChats(String email) {
        List<Chat> chats = chatRepository.findAllByUserEmail(email);
        List<ChatModel> chatModelList = new ArrayList<>();

        for(Chat chat : chats){
            ChatModel chatModel = getChat(chat);
            chatModelList.add(chatModel);
        }

        return chatModelList;
    }

    private ChatModel getChat(Chat chat){
        ChatModel chatModel = new ChatModel();
        chatModel.setChatId(chat.getChatId());
        chatModel.setStoreName(chat.getStore().getStoreName());
        chatModel.setStorePhotoUrl(chat.getStore().getPhotoUrl());
        return chatModel;
    }


}
