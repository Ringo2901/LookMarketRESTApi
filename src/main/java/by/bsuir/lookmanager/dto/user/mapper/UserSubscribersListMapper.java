package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.entities.product.information.ImageData;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserSubscribersMapper.class)
public interface UserSubscribersListMapper {
    List<UserSubscriberResponseDto> toSubscribersList(List<UserEntity> entities);
}
