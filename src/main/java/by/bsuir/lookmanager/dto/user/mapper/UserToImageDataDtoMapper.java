package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface UserToImageDataDtoMapper {
    ImageDataToDtoMapper INSTANCE = Mappers.getMapper(ImageDataToDtoMapper.class);

    @Mapping(target = "imageData", ignore = true)
    ImageDataDto userToImageDataDto (UserEntity user);

    @AfterMapping
    default void mapImageData(UserEntity user, @MappingTarget ImageDataDto imageDataDto) {
        if (user != null && user.getUserProfile().getImageData() != null) {
            imageDataDto.setImageData(Base64.getEncoder().encodeToString(user.getUserProfile().getImageData()));
        }
    }
}
