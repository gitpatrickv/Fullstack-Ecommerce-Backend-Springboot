package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginateUserResponse {

    private List<UserModel> userModels;
    private PageResponse pageResponse;
}
