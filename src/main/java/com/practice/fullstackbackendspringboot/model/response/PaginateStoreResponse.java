package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginateStoreResponse {
    private List<StoreModel> storeModels;
    private PageResponse pageResponse;
}
