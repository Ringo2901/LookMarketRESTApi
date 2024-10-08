package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.*;

public interface UserService {
    ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto);
    ApplicationResponseDto<?> userLogout(Long userId, boolean status);

    ApplicationResponseDto<Long> userLogin(UserLoginRequestDto userLoginRequestDto);

    ApplicationResponseDto<UserProfileResponseDto> findUserById(Long userId, Long id, String lang);

    ApplicationResponseDto<UserProfileResponseDto> saveUserProfileById(Long id, UserProfileRequestDto requestDto, String lang);

    ApplicationResponseDto<?> deleteUserById(Long id);

    ApplicationResponseDto<Long> userGoogleAuth(UserGoogleAuthDto dto);
}
