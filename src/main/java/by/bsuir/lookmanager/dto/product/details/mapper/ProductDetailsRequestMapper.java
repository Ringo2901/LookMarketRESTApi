package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.Condition;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductDetailsRequestMapper {
    @Mappings({
            @Mapping(source = "entity.gender", target = "productInformation.gender", qualifiedByName = "mapGender"),
            @Mapping(source = "entity.season", target = "productInformation.season", qualifiedByName = "mapSeason"),
            @Mapping(source = "entity.condition", target = "productInformation.condition", qualifiedByName = "mapCondition"),
            @Mapping(source = "entity.ageType", target = "productInformation.ageType", qualifiedByName = "mapAgeType"),
            @Mapping(source = "entity.description", target = "productInformation.description"),
            @Mapping(source = "entity.price", target = "productInformation.price")
    })
    ProductEntity productRequestDtoToEntity(ProductDetailsRequestDto entity);

    @Named("mapSeason")
    default Season mapSeason(String season) {
        if (season.isEmpty()){
            return null;
        }
        return "DEMI-SEASON".equals(season) ? Season.DEMI_SEASON : Season.valueOf(season);
    }
    @Named("mapGender")
    default ProductGender mapGender(String gender) {
        if (gender.isEmpty()){
            return null;
        }
        return ProductGender.valueOf(gender);
    }
    @Named("mapCondition")
    default Condition mapCondition(String condition) {
        if (condition.isEmpty()){
            return null;
        }
        return Condition.valueOf(condition);
    }
    @Named("mapAgeType")
    default AgeType mapAgeType(String ageType) {
        if (ageType.isEmpty()){
            return null;
        }
        return AgeType.valueOf(ageType);
    }
}
