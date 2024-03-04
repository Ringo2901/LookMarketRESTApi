package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.dto.user.mapper.UserLoginMapper;
import by.bsuir.lookmanager.dto.user.mapper.UserProfileMapper;
import by.bsuir.lookmanager.dto.user.mapper.UserRegisterMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserRegisterMapper userRegisterMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public ApplicationResponseDto<Object> userRegister(UserRegisterRequestDto userRegisterRequestDto) {
        UserEntity user = userRegisterMapper.userRegisterRequestToUserEntity(userRegisterRequestDto);
        ApplicationResponseDto<Object> userRegisterResponseDto = new ApplicationResponseDto<>();
        if (userRepository.countByEmail(user.getEmail()) > 0) {
            userRegisterResponseDto.setCode(401);
            userRegisterResponseDto.setStatus("Error");
            userRegisterResponseDto.setMessage("Registration failed! A user with this email already exists!");
        } else {
            if (userRepository.countByLogin(user.getLogin()) > 0) {
                userRegisterResponseDto.setCode(401);
                userRegisterResponseDto.setStatus("ERROR");
                userRegisterResponseDto.setMessage("Registration failed! A user with this login already exists!");
            } else {
                userRepository.save(user);
                userRegisterResponseDto.setCode(200);
                userRegisterResponseDto.setStatus("OK");
                userRegisterResponseDto.setMessage("Registration success!");
            }
        }
        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<Object> userLogin(UserLoginRequestDto userLoginRequestDto) {
        UserEntity user = userLoginMapper.userLoginRequestToUserEntity(userLoginRequestDto);
        ApplicationResponseDto<Object> userLoginResponseDto = new ApplicationResponseDto<>();
        if (userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword()).isPresent()) {
            userLoginResponseDto.setCode(200);
            userLoginResponseDto.setStatus("OK");
            userLoginResponseDto.setMessage("Authorization success!");
        } else {
            userLoginResponseDto.setCode(401);
            userLoginResponseDto.setStatus("ERROR");
            userLoginResponseDto.setMessage("Authorization failed!");
        }
        return userLoginResponseDto;
    }

    @Override
    public ApplicationResponseDto<UserProfileResponseDto> findUserById(Long id) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("User not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("User found!");
            responseDto.setPayload(userProfileMapper.userEntityToUserProfileResponseDto(user));
        }
        return responseDto;
    }
}
