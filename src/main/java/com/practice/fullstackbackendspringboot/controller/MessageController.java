package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.SendMessageRequest;
import com.practice.fullstackbackendspringboot.model.response.GetChatMessageByChatIdResponse;
import com.practice.fullstackbackendspringboot.service.MessageService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody SendMessageRequest request) {
        String currentUser = userService.getAuthenticatedUser();
        messageService.sendMessage(currentUser, request);
    }

    @GetMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public GetChatMessageByChatIdResponse getMessagesByChatId(@PathVariable("chatId") Long chatId){
        return messageService.getMessagesByChatId(chatId);
    }
}
