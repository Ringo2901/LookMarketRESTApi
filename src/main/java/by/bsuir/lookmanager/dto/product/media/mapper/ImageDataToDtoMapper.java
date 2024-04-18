package by.bsuir.lookmanager.dto.product.media.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.entities.product.information.ImageData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ImageDataToDtoMapper {
    ImageDataToDtoMapper INSTANCE = Mappers.getMapper(ImageDataToDtoMapper.class);

    @Mapping(target = "imageData", ignore = true)
    ImageDataResponseDto mediaToDto(ImageData media);

    @AfterMapping
    default void mapImageData(ImageData media, @MappingTarget ImageDataResponseDto imageDataResponseDto) {
        if (media != null && media.getImageData() != null) {
            imageDataResponseDto.setImageData(Base64.getEncoder().encodeToString(media.getImageData()));
        }
    }

    @Mapping(target = "imageData", ignore = true)
    ImageData dtoToMedia (ImageDataRequestDto requestDto);

    @AfterMapping
    default void mapImageData(ImageDataRequestDto requestDto, @MappingTarget ImageData imageData) {
        if (requestDto != null && requestDto.getImageData() != null) {
            String carg = requestDto.getImageData();
            carg = carg.replace("\n","");
            imageData.setImageData(Base64.getDecoder().decode(carg));
        }
    }
}