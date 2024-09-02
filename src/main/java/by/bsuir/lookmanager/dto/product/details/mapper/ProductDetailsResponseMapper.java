package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.ProductColor;
import by.bsuir.lookmanager.entities.product.information.ProductMaterial;
import by.bsuir.lookmanager.entities.product.information.ProductSize;
import by.bsuir.lookmanager.entities.product.information.ProductTag;
import by.bsuir.lookmanager.entities.product.information.Season;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductDetailsResponseMapper {
    @Mappings({
            @Mapping(source = "subCategory.nameEn", target = "subCategoryName"),
            @Mapping(source = "subCategory.category.nameEn", target = "categoryName"),
            @Mapping(source = "productInformation.description", target = "description"),
            @Mapping(target = "gender", ignore = true),
            @Mapping(target = "season", ignore = true),
            @Mapping(target = "condition", ignore = true),
            @Mapping(target = "ageType", ignore = true),
            @Mapping(source = "productInformation.productBrand.brandName", target = "brandName"),
            @Mapping(source = "productInformation.sizes", target = "sizes"),
            @Mapping(target = "colors", ignore = true),
            @Mapping(target = "materials", ignore = true),
            @Mapping(target = "tags", ignore = true),
            @Mapping(source = "productInformation.price", target = "price"),
            @Mapping(source = "catalog.user.login", target = "login"),
            @Mapping(source = "catalog.user.id", target = "userId"),
            @Mapping(source = "catalog.user.userProfile.firstname", target = "firstname"),
            @Mapping(source = "catalog.user.userProfile.lastname", target = "lastname"),
            @Mapping(source = "catalog.user.userProfile.userImageUrl", target = "userImageUrl"),
            @Mapping(target = "createdTime", expression = "java(formatTimestamp(product.getCreatedTime()))"),
            @Mapping(target = "updateTime", expression = "java(formatTimestamp(product.getUpdateTime()))"),
            @Mapping(source = "productInformation.latitude", target = "latitude"),
            @Mapping(source = "productInformation.longitude", target = "longitude"),
            @Mapping(source = "productInformation.viewNumber", target = "viewNumber")
    })
    ProductDetailsResponseDto productEntityToResponseDto (ProductEntity product);

    default String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(timestamp);
    }
    default List<Integer> mapProductSizes(List<ProductSize> productSizes) {
        return productSizes.stream()
                .map(ProductSize::getSize)
                .collect(Collectors.toList());
    }
}
