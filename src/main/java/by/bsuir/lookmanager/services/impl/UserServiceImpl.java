package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.CityRepository;
import by.bsuir.lookmanager.dao.CountryRepository;
import by.bsuir.lookmanager.dao.UserProfileRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.dto.user.mapper.UserProfileMapper;
import by.bsuir.lookmanager.dto.user.mapper.UserRegisterMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegisterMapper userRegisterMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto) {
        UserEntity user = userRegisterMapper.userRegisterRequestToUserEntity(userRegisterRequestDto);
        ApplicationResponseDto<?> userRegisterResponseDto = new ApplicationResponseDto<>();
        if (userRegisterRequestDto.getEmail() != null) {
            user.setEmail(userRegisterRequestDto.getEmail());
            if (userRepository.countByEmail(user.getEmail()) > 0) {
                userRegisterResponseDto.setCode(401);
                userRegisterResponseDto.setStatus("Error");
                userRegisterResponseDto.setMessage("Registration failed! A user with this email already exists!");
                return userRegisterResponseDto;
            }
        }
        if (userRegisterRequestDto.getPhone() != null) {
            user.getUserProfile().setPhoneNumber(userRegisterRequestDto.getPhone());
            if (userRepository.countByUserProfilePhoneNumber(userRegisterRequestDto.getPhone()) > 0) {
                userRegisterResponseDto.setCode(401);
                userRegisterResponseDto.setStatus("ERROR");
                userRegisterResponseDto.setMessage("Registration failed! A user with this phone already exists!");
                return userRegisterResponseDto;
            }
        }
        if (userRepository.countByLogin(userRegisterRequestDto.getLogin()) > 0) {
            userRegisterResponseDto.setCode(401);
            userRegisterResponseDto.setStatus("ERROR");
            userRegisterResponseDto.setMessage("Registration failed! A user with this login already exists!");
            return userRegisterResponseDto;
        }
        userProfileRepository.save(user.getUserProfile());
        userRepository.save(user);
        userRegisterResponseDto.setCode(200);
        userRegisterResponseDto.setStatus("OK");
        userRegisterResponseDto.setMessage("Registration success!");

        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<Long> userLogin(UserLoginRequestDto userLoginRequestDto) {
        Optional<UserEntity> user = Optional.empty();
        if (userLoginRequestDto.getPhone() != null) {
            user = userRepository.findByUserProfilePhoneNumberAndPassword(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
        }
        if (userLoginRequestDto.getEmail() != null) {
            user = userRepository.findByEmailAndPassword(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());
        }
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        if (user.isPresent()) {
            userLoginResponseDto.setCode(200);
            userLoginResponseDto.setStatus("OK");
            userLoginResponseDto.setMessage("Authorization success!");
            userLoginResponseDto.setPayload(user.get().getId());
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

    @Override
    public ApplicationResponseDto<UserProfileResponseDto> saveUserProfileById(Long id, UserProfileRequestDto requestDto) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("User not found!");
        } else {
            UserProfile userProfile = user.getUserProfile();
            userProfile.setFirstname(requestDto.getFirstname());
            userProfile.setLastname(requestDto.getLastname());
            userProfile.setDateOfBirth(requestDto.getDateOfBirth());
            userProfile.setAddress(requestDto.getAddress());
            userProfile.setPhoneNumber(requestDto.getPhoneNumber());
            userProfile.setGender(requestDto.getGender());
            userProfile.setPostalCode(requestDto.getPostalCode());
            if (requestDto.getImageData() != null) {
                userProfile.setImageData(Base64.getDecoder().decode(requestDto.getImageData()));
            } else {
                userProfile.setImageData(null);
            }
            userProfile.setCity(cityRepository.getReferenceById(requestDto.getCityId()));
            userProfile.setCountry(countryRepository.getReferenceById(requestDto.getCountryId()));
            user.setUserProfile(userProfile);
            user = userRepository.save(user);
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("User profile update!");
            responseDto.setPayload(userProfileMapper.userEntityToUserProfileResponseDto(user));
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> deleteUserById(Long id) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("User not found!");
        } else {
            userProfileRepository.delete(user.getUserProfile());
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("User delete!");
        }
        return responseDto;
    }
}
