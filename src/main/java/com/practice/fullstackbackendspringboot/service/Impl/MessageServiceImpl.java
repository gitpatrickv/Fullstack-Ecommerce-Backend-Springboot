package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Chat;
import com.practice.fullstackbackendspringboot.entity.Message;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.MessageModel;
import com.practice.fullstackbackendspringboot.model.request.SendMessageRequest;
import com.practice.fullstackbackendspringboot.model.response.GetChatMessageByChatIdResponse;
import com.practice.fullstackbackendspringboot.repository.ChatRepository;
import com.practice.fullstackbackendspringboot.repository.MessageRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.MessageService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Override
    public void sendMessage(String user, SendMessageRequest request) {
        Optional<User> currentUser = userRepository.findByEmail(user);
        Optional<Chat> chatOptional = chatRepository.findById(request.getChatId());

        if(currentUser.isPresent() && chatOptional.isPresent()){
            Message message = new Message();
            message.setSender(currentUser.get().getEmail());
            message.setContent(request.getContent());
            message.setTimestamp(LocalDateTime.now());
            message.setChat(chatOptional.get());
            messageRepository.save(message);
        }
    }

    @Override
    public GetChatMessageByChatIdResponse getMessagesByChatId(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);

        if(chatOptional.isPresent()){
            Chat chat = chatOptional.get();
            GetChatMessageByChatIdResponse getChatMessageByChatIdResponse = new GetChatMessageByChatIdResponse();

            List<Message> messages = messageRepository.findAllByChat_ChatId(chatId);
            List<MessageModel> messageModelList = new ArrayList<>();

            for(Message message : messages) {
                MessageModel messageModel = messageMapper.mapEntityToModel(message);
                messageModel.setChatId(chat.getChatId());
                messageModelList.add(messageModel);
            }

            getChatMessageByChatIdResponse.setMessageModelList(messageModelList);
            return getChatMessageByChatIdResponse;
        }
        else {
            throw new NoSuchElementException(StringUtil.CHAT_NOT_FOUND + chatId);
        }

    }
}
