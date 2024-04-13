package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.*;

public interface UserService {
    ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto);

    ApplicationResponseDto<Long> userLogin(UserLoginRequestDto userLoginRequestDto);

    ApplicationResponseDto<UserProfileResponseDto> findUserById(Long userId, Long id);

    ApplicationResponseDto<UserProfileResponseDto> saveUserProfileById(Long id, UserProfileRequestDto requestDto);

    ApplicationResponseDto<?> deleteUserById(Long id);

    ApplicationResponseDto<Long> userGoogleAuth(UserGoogleAuthDto dto);
}
