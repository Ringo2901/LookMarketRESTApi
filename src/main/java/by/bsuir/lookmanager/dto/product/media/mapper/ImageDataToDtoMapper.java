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


    ImageDataResponseDto mediaToDto(ImageData media);

    ImageData dtoToMedia (ImageDataRequestDto requestDto);

}