package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.SendMessageRequest;

public interface MessageService {

    void sendMessage(String user, SendMessageRequest request);
}
