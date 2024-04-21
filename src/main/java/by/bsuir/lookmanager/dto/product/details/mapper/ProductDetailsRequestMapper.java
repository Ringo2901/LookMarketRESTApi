package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.enums.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductDetailsRequestMapper {
    @Mappings({
            @Mapping(source = "entity.gender", target = "productInformation.gender"),
            @Mapping(source = "entity.season", target = "productInformation.season", qualifiedByName = "mapSeason"),
            @Mapping(source = "entity.condition", target = "productInformation.condition"),
            @Mapping(source = "entity.description", target = "productInformation.description"),
            @Mapping(source = "entity.price", target = "productInformation.price")
    })
    ProductEntity productRequestDtoToEntity(ProductDetailsRequestDto entity);

    @Named("mapSeason")
    default Season mapSeason(String season) {
        return "DEMI-SEASON".equals(season) ? Season.DEMI_SEASON : Season.valueOf(season);
    }
}
