package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = GeneralProductResponseMapper.class)
public interface ProductListMapper {
    List<GeneralProductResponseDto> toGeneralProductResponseDtoList(List<ProductEntity> entities);
}
