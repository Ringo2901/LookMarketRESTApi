package by.bsuir.lookmanager.dto.product.mapper;

import by.bsuir.lookmanager.dto.product.ProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductInformationMapper {
    ProductResponseDto productEntityToResponseDto (ProductEntity product);
}
