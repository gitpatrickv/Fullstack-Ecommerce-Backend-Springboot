package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.ChatModel;
import com.practice.fullstackbackendspringboot.model.response.ChatIdResponse;
import com.practice.fullstackbackendspringboot.service.ChatService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/{recipient}")
    @ResponseStatus(HttpStatus.OK)
    public ChatIdResponse createChat(@PathVariable("recipient") String recipient){
        String sender = userService.getAuthenticatedUser();
        return chatService.createChat(sender, recipient);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ChatModel> getAllUserChats(){
        String user = userService.getAuthenticatedUser();
        return chatService.getAllUserChats(user);
    }

}
