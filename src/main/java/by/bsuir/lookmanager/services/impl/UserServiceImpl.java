package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.*;
import by.bsuir.lookmanager.dto.user.mapper.UserProfileMapper;
import by.bsuir.lookmanager.dto.user.mapper.UserRegisterMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import by.bsuir.lookmanager.exceptions.BadParameterValueException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
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
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto) throws BadParameterValueException {
        UserEntity user = userRegisterMapper.userRegisterRequestToUserEntity(userRegisterRequestDto);
        ApplicationResponseDto<?> userRegisterResponseDto = new ApplicationResponseDto<>();
        if (userRegisterRequestDto.getEmail() != null) {
            user.setEmail(userRegisterRequestDto.getEmail());
            if (userRepository.countByEmail(user.getEmail()) > 0) {
                throw new BadParameterValueException("Registration failed! A user with this email already exists!");
            }
        }
        if (userRegisterRequestDto.getPhone() != null) {
            user.getUserProfile().setPhoneNumber(userRegisterRequestDto.getPhone());
            if (userRepository.countByUserProfilePhoneNumber(userRegisterRequestDto.getPhone()) > 0) {
                throw new BadParameterValueException("Registration failed! A user with this phone already exists!");
            }
        }
        if (userRepository.countByLogin(userRegisterRequestDto.getLogin()) > 0) {
            throw new BadParameterValueException("Registration failed! A user with this login already exists!");
        }
        userProfileRepository.save(user.getUserProfile());
        userRepository.save(user);
        userRegisterResponseDto.setCode(201);
        userRegisterResponseDto.setStatus("OK");
        userRegisterResponseDto.setMessage("Registration success!");

        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<Long> userLogin(UserLoginRequestDto userLoginRequestDto) throws NotFoundException, BadParameterValueException {
        UserEntity user = null;
        if (userLoginRequestDto.getPhone() != null) {
            user = userRepository.findByUserProfilePhoneNumberAndPassword(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword()).orElseThrow(() -> new NotFoundException("Authorization failed!"));
        }
        if (userLoginRequestDto.getEmail() != null) {
            user = userRepository.findByEmailAndPassword(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()).orElseThrow(() -> new NotFoundException("Authorization failed!"));
        }
        if (user == null) {
            throw new BadParameterValueException("User not found, not enough parameters");
        }
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        userLoginResponseDto.setCode(200);
        userLoginResponseDto.setStatus("OK");
        userLoginResponseDto.setMessage("Authorization success!");
        userLoginResponseDto.setPayload(user.getId());
        return userLoginResponseDto;
    }

    @Override
    public ApplicationResponseDto<UserProfileResponseDto> findUserById(Long id) throws NotFoundException {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        List<Catalog> catalogs = catalogRepository.findByUserId(user.getId());
        UserProfileResponseDto userProfileResponseDto = userProfileMapper.userEntityToUserProfileResponseDto(user);
        for (Catalog catalog: catalogs){
            userProfileResponseDto.getCatalogsIdList().add(catalog.getId());
        }
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("User found!");
        responseDto.setPayload(userProfileResponseDto);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<UserProfileResponseDto> saveUserProfileById(Long id, UserProfileRequestDto requestDto) throws NotFoundException {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
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
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("User profile update!");
        responseDto.setPayload(userProfileMapper.userEntityToUserProfileResponseDto(user));
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> deleteUserById(Long id) throws NotFoundException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        userProfileRepository.delete(user.getUserProfile());
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("User delete!");
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Long> userGoogleAuth(UserGoogleAuthDto dto) {
        Optional<UserEntity> user = userRepository.findByEmail(dto.getEmail());
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        if (user.isEmpty()) {
            UserEntity userToSave = new UserEntity();
            userToSave.setEmail(dto.getEmail());
            UserProfile profile = new UserProfile();
            profile.setFirstname(dto.getFirstname());
            profile.setLastname(dto.getLastname());
            userToSave.setUserProfile(profile);
            userProfileRepository.save(profile);
            userToSave = userRepository.save(userToSave);
            userLoginResponseDto.setPayload(userToSave.getId());
        } else {
            userLoginResponseDto.setPayload(user.get().getId());
        }
        userLoginResponseDto.setCode(200);
        userLoginResponseDto.setStatus("OK");
        userLoginResponseDto.setMessage("Authorization success!");
        return userLoginResponseDto;
    }
}
