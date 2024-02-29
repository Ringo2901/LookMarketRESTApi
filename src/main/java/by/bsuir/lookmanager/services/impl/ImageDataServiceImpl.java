package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataListToDto;
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
    private ImageDataListToDto imageDataToDtoMapper;

    @Override
    public ApplicationResponseDto<List<ImageDataDto>> getImageDataById(List<Long> id) {
        ApplicationResponseDto<List<ImageDataDto>> responseDto = new ApplicationResponseDto<>();
        List<ImageData> imageData = imageDataRepository.findByIdIn(id);
        if (imageData == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("Images not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Images found!");
            responseDto.setPayload(imageDataToDtoMapper.toImageDataDtoList(imageData));
        }
        return responseDto;
    }
}
