package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.user.*;

public interface UserService {
    ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto);
    ApplicationResponseDto<?> userLogin(UserLoginRequestDto userLoginRequestDto);
    ApplicationResponseDto<UserProfileResponseDto> findUserById(Long id);
}
