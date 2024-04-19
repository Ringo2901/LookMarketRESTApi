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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @Autowired
    private SubscriptionRepository subscriptionRepository;

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
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        userProfileRepository.save(user.getUserProfile());
        UserEntity savedUser = userRepository.save(user);
        Catalog personalCatalog = new Catalog();
        personalCatalog.setName("Personal catalog");
        personalCatalog.setUser(savedUser);
        catalogRepository.save(personalCatalog);
        userRegisterResponseDto.setCode(201);
        userRegisterResponseDto.setStatus("OK");
        userRegisterResponseDto.setMessage("Registration success!");

        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<?> userLogout(Long userId, boolean status) {
        ApplicationResponseDto<?> userRegisterResponseDto =  new ApplicationResponseDto<>();
        userRegisterResponseDto.setCode(403);
        userRegisterResponseDto.setStatus("ERROR");
        if (userId != null) {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
            user.setAuthorisationStatus(status);
            user.setLastSignIn(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            userRegisterResponseDto.setCode(200);
            userRegisterResponseDto.setStatus("OK");
            userRegisterResponseDto.setMessage("Status change success!");
        }
        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<Long> userLogin(UserLoginRequestDto userLoginRequestDto) throws NotFoundException, BadParameterValueException {
        UserEntity user = null;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userLoginRequestDto.getPhone() != null) {
            user = userRepository.findByUserProfilePhoneNumber(userLoginRequestDto.getPhone()).orElseThrow(() -> new NotFoundException("Authorization failed!"));
        }
        if (userLoginRequestDto.getEmail() != null) {
            user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(() -> new NotFoundException("Authorization failed!"));
        }
        if (user == null) {
            throw new BadParameterValueException("User not found, not enough parameters");
        }
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())){
            throw new NotFoundException("Invalid password error!");
        }
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        user.setAuthorisationStatus(true);
        userRepository.save(user);
        userLoginResponseDto.setCode(200);
        userLoginResponseDto.setStatus("OK");
        userLoginResponseDto.setMessage("Authorization success!");
        userLoginResponseDto.setPayload(user.getId());
        return userLoginResponseDto;
    }



    @Override
    public ApplicationResponseDto<UserProfileResponseDto> findUserById(Long userId, Long id) throws NotFoundException {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        List<Catalog> catalogs = catalogRepository.findByUserId(user.getId());
        UserProfileResponseDto userProfileResponseDto = userProfileMapper.userEntityToUserProfileResponseDto(user);
        List<Long> catalogIdsList = new ArrayList<>();
        for (Catalog catalog: catalogs){
            catalogIdsList.add(catalog.getId());
        }
        userProfileResponseDto.setSubscribed(subscriptionRepository.existsBySubscriberIdAndSellerId(userId, id));
        userProfileResponseDto.setCatalogsIdList(catalogIdsList);
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
        if (requestDto.getDateOfBirth() != null && !requestDto.getDateOfBirth().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(requestDto.getDateOfBirth(), formatter);
            userProfile.setDateOfBirth(Date.valueOf(localDate));
        } else {
            userProfile.setDateOfBirth(null);
        }
        userProfile.setAddress(requestDto.getAddress());
        userProfile.setPhoneNumber(requestDto.getPhoneNumber());
        userProfile.setGender(requestDto.getGender());
        userProfile.setPostalCode(requestDto.getPostalCode());
        if (requestDto.getImageData() != null) {
            String carg = requestDto.getImageData();
            carg = carg.replace("\n","");
            userProfile.setImageData(Base64.getDecoder().decode(carg));
        } else {
            userProfile.setImageData(null);
        }
        if (cityRepository.existsById(requestDto.getCityId())) {
            userProfile.setCity(cityRepository.getReferenceById(requestDto.getCityId()));
        } else {
            userProfile.setCity(null);
        }
        if (countryRepository.existsById(requestDto.getCountryId())) {
            userProfile.setCountry(countryRepository.getReferenceById(requestDto.getCountryId()));
        } else {
            userProfile.setCountry(null);
        }
        user.setUserProfile(userProfile);
        user.setLogin(requestDto.getLogin());
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
