package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataListToDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.information.ImageData;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.ImageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageDataServiceImpl implements ImageDataService {
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataListToDto imageDataToListDtoMapper;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;

    @Override
    public ApplicationResponseDto<List<ImageDataResponseDto>> getImageDataByProductId(Long id) throws NotFoundException {
        ApplicationResponseDto<List<ImageDataResponseDto>> responseDto = new ApplicationResponseDto<>();
        List<ImageData> imageData = imageDataRepository.findByProductId(id);
        if (imageData == null) {
            throw new NotFoundException("Images not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Images found!");
            responseDto.setPayload(imageDataToListDtoMapper.toImageDataDtoList(imageData));
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Object> addImageDataByProductId(Long id, ImageDataRequestDto requestDto) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        ImageData imageData = imageDataToDtoMapper.dtoToMedia(requestDto);
        imageData.setProductId(id);
        imageDataRepository.save(imageData);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Image add!");
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Object> deleteImageDataById(Long id) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        imageDataRepository.deleteById(id);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Images delete!");
        return responseDto;
    }
}
