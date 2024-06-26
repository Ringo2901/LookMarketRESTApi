package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;

import java.util.List;

public interface FavoritesService {
    ApplicationResponseDto<List<GeneralProductResponseDto>> getFavoritesByUserId(Long id, Integer pageNumber, Integer pageSize);
    ApplicationResponseDto<List<GeneralProductResponseDto>> getFavoritesByUserIdWithoutCurrent(Long id, Long currentId, Integer pageNumber, Integer pageSize);
    ApplicationResponseDto<?> addFavorite(Long userId, Long productId);
    ApplicationResponseDto<?> deleteFavorite(Long userId, Long productId);
}
