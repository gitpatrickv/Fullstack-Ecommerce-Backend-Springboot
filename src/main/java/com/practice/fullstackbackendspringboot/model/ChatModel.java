package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel {
    private Long chatId;
    private String name;
    private String photoUrl;
    private String content;
    @JsonFormat(pattern = "MM/dd")
    private LocalDateTime timestamp;

}
