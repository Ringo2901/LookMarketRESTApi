package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDetailsRequestMapper {
    @Mapping(source = "entity.gender", target = "productInformation.gender")
    @Mapping(source = "entity.season", target = "productInformation.season")
    @Mapping(source = "entity.condition", target = "productInformation.condition")
    @Mapping(source = "entity.description", target = "productInformation.description")
    @Mapping(source = "entity.price", target = "productInformation.price")
    ProductEntity productRequestDtoToEntity (ProductDetailsRequestDto entity);
}
