package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    @Mapping(source = "firstname", target = "userProfile.firstname")
    @Mapping(source = "lastname", target = "userProfile.lastname")
    UserEntity userRegisterRequestToUserEntity(UserRegisterRequestDto userRegisterRequestDto);
}
