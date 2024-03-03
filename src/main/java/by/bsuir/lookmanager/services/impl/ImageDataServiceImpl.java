package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataListToDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.information.ImageData;
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
    public ApplicationResponseDto<List<ImageDataResponseDto>> getImageDataByProductId(Long id) {
        ApplicationResponseDto<List<ImageDataResponseDto>> responseDto = new ApplicationResponseDto<>();
        List<ImageData> imageData = imageDataRepository.findByProductId(id);
        if (imageData == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("Images not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Images found!");
            responseDto.setPayload(imageDataToListDtoMapper.toImageDataDtoList(imageData));
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<ImageDataResponseDto> getFirstImageDataByProductId(Long id) {
        ApplicationResponseDto<ImageDataResponseDto> responseDto = new ApplicationResponseDto<>();
        ImageData imageData = imageDataRepository.findFirstByProductId(id);
        if (imageData == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("Images not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Images found!");
            responseDto.setPayload(imageDataToDtoMapper.mediaToDto(imageData));
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<ImageDataResponseDto>> addImageDataByProductId(Long id, ImageDataRequestDto requestDto) {
        ImageData imageData = imageDataToDtoMapper.dtoToMedia(requestDto);
        imageData.setProductId(id);
        imageDataRepository.save(imageData);
        return getImageDataByProductId(id);
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
