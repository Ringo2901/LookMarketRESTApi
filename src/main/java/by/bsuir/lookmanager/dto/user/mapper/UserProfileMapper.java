package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    ImageDataToDtoMapper INSTANCE = Mappers.getMapper(ImageDataToDtoMapper.class);

    @Mapping(target = "imageData", ignore = true)
    @Mapping(source = "userProfile.lastname", target = "lastname")
    @Mapping(source = "userProfile.firstname", target = "firstname")
    @Mapping(source = "userProfile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "userProfile.address", target = "address")
    @Mapping(source = "userProfile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "userProfile.gender", target = "gender")
    @Mapping(source = "userProfile.postalCode", target = "postalCode")
    @Mapping(source = "userProfile.city.name", target = "cityName")
    @Mapping(source = "userProfile.country.name", target = "countryName")
    @Mapping(target = "lastSignIn", expression = "java(formatTimestamp(entity.getLastSignIn()))")
    UserProfileResponseDto userEntityToUserProfileResponseDto(UserEntity entity);

    default String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(timestamp);
    }
    @AfterMapping
    default void mapImageData(UserEntity user, @MappingTarget UserProfileResponseDto userProfileResponseDto) {
        if (user != null && user.getUserProfile().getImageData() != null) {
            userProfileResponseDto.setImageData(Base64.getEncoder().encodeToString(user.getUserProfile().getImageData()));
        }
    }
}
