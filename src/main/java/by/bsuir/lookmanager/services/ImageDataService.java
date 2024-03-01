package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataDto;

import java.util.List;

public interface ImageDataService {
    ApplicationResponseDto<List<ImageDataDto>> getImageDataByProductId(Long id);
    ApplicationResponseDto<ImageDataDto> getFirstImageDataByProductId(Long id);
}
