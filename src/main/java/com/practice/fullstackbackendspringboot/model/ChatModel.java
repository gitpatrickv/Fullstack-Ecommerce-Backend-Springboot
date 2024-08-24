package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel {
    private Long chatId;
    private String storeName;
    private String storePhotoUrl;
    List<MessageModel> messageModelList = new ArrayList<>();
//    private String content;
//    private String timestamp;

}
