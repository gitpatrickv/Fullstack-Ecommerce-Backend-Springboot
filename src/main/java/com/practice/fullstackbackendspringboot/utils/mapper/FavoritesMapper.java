package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Favorites;
import com.practice.fullstackbackendspringboot.model.FavoritesModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FavoritesMapper {
    private final ModelMapper mapper = new ModelMapper();

    public FavoritesModel mapEntityToModel(Favorites favorites){
        return mapper.map(favorites, FavoritesModel.class);
    }

    public Favorites mapModelToEntity(FavoritesModel favoritesModel){
        return mapper.map(favoritesModel, Favorites.class);
    }
}
