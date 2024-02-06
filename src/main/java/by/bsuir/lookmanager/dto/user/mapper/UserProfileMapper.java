package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.entities.UserEntity;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto userEntityToUserProfileResponseDto(UserEntity entity);
}
