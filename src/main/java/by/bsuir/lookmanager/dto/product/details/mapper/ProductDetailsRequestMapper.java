package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.AgeType;
import by.bsuir.lookmanager.entities.product.information.Condition;
import by.bsuir.lookmanager.entities.product.information.ProductGender;
import by.bsuir.lookmanager.entities.product.information.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductDetailsRequestMapper {
    @Mappings({
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "productInformation.gender", ignore = true),
            @Mapping(target = "productInformation.season", ignore = true),
            @Mapping(target = "productInformation.condition", ignore = true),
            @Mapping(target = "productInformation.ageType", ignore = true),
            @Mapping(source = "entity.description", target = "productInformation.description"),
            @Mapping(source = "entity.price", target = "productInformation.price"),
            @Mapping(source = "entity.latitude", target = "productInformation.latitude"),
            @Mapping(source = "entity.longitude", target = "productInformation.longitude")
    })
    ProductEntity productRequestDtoToEntity(ProductDetailsRequestDto entity);
}
