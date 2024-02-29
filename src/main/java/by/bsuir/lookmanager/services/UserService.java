package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.dto.user.*;

public interface UserService {
    ApplicationResponseDto<Object> userRegister(UserRegisterRequestDto userRegisterRequestDto);
    ApplicationResponseDto<Object> userLogin(UserLoginRequestDto userLoginRequestDto);
    ApplicationResponseDto<UserProfileResponseDto> findUserById(Long id);
    ApplicationResponseDto<ImageDataDto> findProfileImageByUserId(Long id);
}
