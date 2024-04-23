package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.UserGender;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    ImageDataToDtoMapper INSTANCE = Mappers.getMapper(ImageDataToDtoMapper.class);

    @Mappings({
            @Mapping(source = "userProfile.lastname", target = "lastname"),
            @Mapping(source = "userProfile.firstname", target = "firstname"),
            @Mapping(target = "dateOfBirth", ignore = true),
            @Mapping(target = "registrationDate", ignore = true),
            @Mapping(source = "userProfile.address", target = "address"),
            @Mapping(source = "userProfile.phoneNumber", target = "phoneNumber"),
            @Mapping(source = "userProfile.gender", target = "gender", qualifiedByName = "mapGender"),
            @Mapping(source = "userProfile.postalCode", target = "postalCode"),
            @Mapping(source = "userProfile.city.id", target = "cityId"),
            @Mapping(source = "userProfile.country.id", target = "countryId"),
            @Mapping(source = "userProfile.latitude", target = "latitude"),
            @Mapping(source = "userProfile.longitude", target = "longitude"),
            @Mapping(target = "lastSignIn", expression = "java(formatTimestamp(entity.getLastSignIn()))")
    })
    UserProfileResponseDto userEntityToUserProfileResponseDto(UserEntity entity);

    @Named("mapGender")
    default UserGender mapGender(String gender) {
        if (gender.isEmpty()) {
            return null;
        }
        return UserGender.valueOf(gender);
    }

    default String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(timestamp);
    }

    default String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(date);
    }

    @AfterMapping
    default void mapDate(UserEntity user, @MappingTarget UserProfileResponseDto userProfileResponseDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        if (user != null) {
            if (user.getUserProfile().getDateOfBirth() != null) {
                userProfileResponseDto.setDateOfBirth(sdf.format(user.getUserProfile().getDateOfBirth()));
                if (user.getRegistrationDate() != null) {
                    userProfileResponseDto.setRegistrationDate(sdf.format(user.getRegistrationDate()));
                }
            }
            if (user.getRegistrationDate() != null) {
                userProfileResponseDto.setRegistrationDate(sdf.format(user.getRegistrationDate()));
            }
        }
    }
}
