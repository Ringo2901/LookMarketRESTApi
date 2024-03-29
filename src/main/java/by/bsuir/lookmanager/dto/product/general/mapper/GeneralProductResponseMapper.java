package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeneralProductResponseMapper {
    @Mapping(source = "subCategory.name", target = "subCategoryName")
    @Mapping(source = "subCategory.category.name", target = "categoryName")
    @Mapping(source = "catalog.user.login", target = "login")
    @Mapping(source = "catalog.user.id", target = "userId")
    GeneralProductResponseDto productEntityToResponseDto(ProductEntity product);
}
