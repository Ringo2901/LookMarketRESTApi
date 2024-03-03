package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSubscribersMapper {
    UserSubscriberResponseDto subscribersToResponseDto (UserEntity user);

}
