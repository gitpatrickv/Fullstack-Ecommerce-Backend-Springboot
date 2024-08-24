package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetChatByIdResponse {

    List<MessageModel> messageModelList = new ArrayList<>();
}
