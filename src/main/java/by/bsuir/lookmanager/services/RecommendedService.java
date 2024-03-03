package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;

import java.util.List;

public interface RecommendedService {
    ApplicationResponseDto<List<GeneralProductResponseDto>> findRecommendedProducts (Long userId, Long numberOfRecommendedItems);
}
