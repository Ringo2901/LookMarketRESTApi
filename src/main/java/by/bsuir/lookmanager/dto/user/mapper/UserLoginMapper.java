package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.entities.UserEntity;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    UserEntity userLoginRequestToUserEntity(UserLoginRequestDto userLoginRequestDto);
}
