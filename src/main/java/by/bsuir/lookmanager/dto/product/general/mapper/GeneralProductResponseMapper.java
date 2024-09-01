package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface GeneralProductResponseMapper {
    @Mapping(source = "subCategory.nameEn", target = "subCategoryName")
    @Mapping(source = "subCategory.category.nameEn", target = "categoryName")
    @Mapping(source = "catalog.user.login", target = "login")
    @Mapping(source = "catalog.user.id", target = "userId")
    @Mapping(source = "productInformation.price", target = "price")
    @Mapping(target = "createdTime", expression = "java(formatTimestamp(product.getCreatedTime()))")
    @Mapping(target = "updateTime", expression = "java(formatTimestamp(product.getUpdateTime()))")
    @Mapping(target = "userImageUrl", source = "catalog.user.userProfile.userImageUrl")
    GeneralProductResponseDto productEntityToResponseDto(ProductEntity product);

    default String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(timestamp);
    }
}
