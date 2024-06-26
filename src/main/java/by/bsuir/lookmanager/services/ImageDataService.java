package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;

import java.util.List;

public interface ImageDataService {
    ApplicationResponseDto<List<ImageDataResponseDto>> getImageDataByProductId(Long id);
    ApplicationResponseDto<ImageDataResponseDto> getImageDataById(Long id);
    ApplicationResponseDto<Object> addImageDataByProductId(Long id, ImageDataRequestDto requestDto);
    ApplicationResponseDto<Object> deleteImageDataById(Long id);
}
