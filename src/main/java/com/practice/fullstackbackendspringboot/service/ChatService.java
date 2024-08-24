package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.ChatModel;
import com.practice.fullstackbackendspringboot.model.response.ChatIdResponse;
import com.practice.fullstackbackendspringboot.model.response.GetChatByIdResponse;

import java.util.List;

public interface ChatService {

    ChatIdResponse createChat(String sender, String recipient);
    GetChatByIdResponse getChatById(Long chatId);
    List<ChatModel> getAllUserChats(String email);
}
