package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Message;
import com.practice.fullstackbackendspringboot.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageMapper {

    private final ModelMapper mapper = new ModelMapper();

    public MessageModel mapEntityToModel(Message message){
        return mapper.map(message, MessageModel.class);
    }

    public Message mapModelToEntity(MessageModel messageModel){
        return mapper.map(messageModel, Message.class);
    }
}
