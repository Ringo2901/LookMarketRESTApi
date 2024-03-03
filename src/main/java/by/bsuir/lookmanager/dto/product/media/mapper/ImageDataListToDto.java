package by.bsuir.lookmanager.dto.product.media.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.entities.product.information.ImageData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImageDataToDtoMapper.class)
public interface ImageDataListToDto {
    List<ImageDataResponseDto> toImageDataDtoList(List<ImageData> entities);
}
