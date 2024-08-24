package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.ChatModel;

import java.util.List;

public interface ChatService {

    void createChat(String sender, String recipient);
    ChatModel getChatById(Long chatId);
    List<ChatModel> getAllUserChats(String email);
}
