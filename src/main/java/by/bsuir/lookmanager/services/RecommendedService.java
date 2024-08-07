package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.ListResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;

import java.util.List;

public interface RecommendedService {
    ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> findRecommendedProducts (Long userId, Integer pageNumber, Integer pageSize);
}
