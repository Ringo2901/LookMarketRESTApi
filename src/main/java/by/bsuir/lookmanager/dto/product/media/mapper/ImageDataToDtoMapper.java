package by.bsuir.lookmanager.dto.product.media.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.entities.product.information.ImageData;
import by.bsuir.lookmanager.entities.product.information.Media;
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
    ImageDataDto mediaToDto(ImageData media);

    @AfterMapping
    default void mapImageData(ImageData media, @MappingTarget ImageDataDto imageDataDto) {
        if (media != null && media.getImageData() != null) {
            imageDataDto.setImageData(Base64.getEncoder().encodeToString(media.getImageData()));
        }
    }
}