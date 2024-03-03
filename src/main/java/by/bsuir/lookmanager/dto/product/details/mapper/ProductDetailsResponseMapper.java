package by.bsuir.lookmanager.dto.product.details.mapper;

import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.ProductColor;
import by.bsuir.lookmanager.entities.product.information.ProductMaterial;
import by.bsuir.lookmanager.entities.product.information.ProductSize;
import by.bsuir.lookmanager.entities.product.information.ProductTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductDetailsResponseMapper {
    @Mapping(source = "subCategory.name", target = "subCategoryName")
    @Mapping(source = "subCategory.category.name", target = "categoryName")
    @Mapping(source = "productInformation.description", target = "description")
    @Mapping(source = "productInformation.gender", target = "gender")
    @Mapping(source = "productInformation.season", target = "season")
    @Mapping(source = "productInformation.condition", target = "condition")
    @Mapping(source = "productInformation.ageType", target = "ageType")
    @Mapping(source = "productInformation.productBrand.brandName", target = "brandName")
    @Mapping(source = "productInformation.sizes", target = "sizes")
    @Mapping(source = "productInformation.colors", target = "colors")
    @Mapping(source = "productInformation.materials", target = "materials")
    @Mapping(source = "productInformation.tags", target = "tags")
    @Mapping(source = "productInformation.price", target = "price")
    @Mapping(source = "catalog.user.login", target = "login")
    @Mapping(source = "catalog.user.id", target = "userId")
    ProductDetailsResponseDto productEntityToResponseDto (ProductEntity product);

    default List<Integer> mapProductSizes(List<ProductSize> productSizes) {
        return productSizes.stream()
                .map(ProductSize::getSize)
                .collect(Collectors.toList());
    }
    default List<String> mapProductColors(List<ProductColor> productColors) {
        return productColors.stream()
                .map(ProductColor::getName)
                .collect(Collectors.toList());
    }
    default List<String> mapProductMaterials(List<ProductMaterial> productMaterials) {
        return productMaterials.stream()
                .map(ProductMaterial::getMaterialName)
                .collect(Collectors.toList());
    }
    default List<String> mapProductTags(List<ProductTag> productTags) {
        return productTags.stream()
                .map(ProductTag::getTagName)
                .collect(Collectors.toList());
    }
}
