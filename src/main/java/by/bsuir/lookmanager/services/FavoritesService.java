package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;

import java.util.List;

public interface FavoritesService {
    ApplicationResponseDto<List<GeneralProductResponseDto>> getFavouritesByUserId (Long id);
    ApplicationResponseDto<?> addFavourite (Long userId, Long productId);
    ApplicationResponseDto<?> deleteFavourite (Long userId, Long productId);
}
