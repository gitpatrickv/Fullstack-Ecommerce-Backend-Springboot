package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.SendMessageRequest;
import com.practice.fullstackbackendspringboot.model.response.GetChatMessageByChatIdResponse;

public interface MessageService {

    void sendMessage(String user, SendMessageRequest request);
    GetChatMessageByChatIdResponse getMessagesByChatId(Long chatId);
}
