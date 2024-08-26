package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.SendMessageRequest;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload SendMessageRequest request) {

        if (request.getChatId() == null) {
            throw new IllegalArgumentException(StringUtil.CHAT_ID_NOT_NULL);
        }

        String destination = StringUtil.USER_ENDPOINT2 + request.getChatId() + StringUtil.MESSAGES_ENDPOINT;
        simpMessagingTemplate.convertAndSend(destination, request);

    }

}

