package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {
    private Long messageId;
    private String sender;
    private String content;
    @JsonFormat(pattern = "H:mm")
    private LocalDateTime timestamp;
    private Long chatId;

}
