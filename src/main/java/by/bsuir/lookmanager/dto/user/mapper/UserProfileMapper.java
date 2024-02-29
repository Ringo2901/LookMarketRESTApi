package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(source = "userProfile.lastname", target = "lastname")
    @Mapping(source = "userProfile.firstname", target = "firstname")
    @Mapping(source = "userProfile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "userProfile.address", target = "address")
    @Mapping(source = "userProfile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "userProfile.gender", target = "gender")
    @Mapping(source = "userProfile.postalCode", target = "postalCode")
    @Mapping(source = "userProfile.city.name", target = "cityName")
    @Mapping(source = "userProfile.country.name", target = "countryName")
    UserProfileResponseDto userEntityToUserProfileResponseDto(UserEntity entity);
}
