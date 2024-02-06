package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.entities.UserEntity;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    UserEntity userRegisterRequestToUserEntity(UserRegisterRequestDto userRegisterRequestDto);
}
