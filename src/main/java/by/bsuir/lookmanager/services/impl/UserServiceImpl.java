package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.*;
import by.bsuir.lookmanager.dto.user.mapper.UserProfileMapper;
import by.bsuir.lookmanager.dto.user.mapper.UserRegisterMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import by.bsuir.lookmanager.entities.user.information.UserGender;
import by.bsuir.lookmanager.exceptions.BadParameterValueException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.moesif.api.models.UserBuilder;
import com.moesif.api.models.UserModel;
import com.moesif.servlet.MoesifFilter;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private Filter moesifFilter;
    private final Cloudinary cloudinary;
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    UserServiceImpl() {
        Dotenv dotenv = Dotenv.load();
        cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
    }

    @Override
    public ApplicationResponseDto<?> userRegister(UserRegisterRequestDto userRegisterRequestDto) throws BadParameterValueException {

        UserEntity user = userRegisterMapper.userRegisterRequestToUserEntity(userRegisterRequestDto);
        ApplicationResponseDto<?> userRegisterResponseDto = new ApplicationResponseDto<>();
        LOGGER.info("Check email");
        if (userRegisterRequestDto.getEmail() != null) {
            user.setEmail(userRegisterRequestDto.getEmail());
            if (userRepository.countByEmail(user.getEmail()) > 0) {
                throw new BadParameterValueException("Registration failed! A user with this email already exists!");
            }
        }
        LOGGER.info("Check phone");
        if (userRegisterRequestDto.getPhone() != null) {
            user.getUserProfile().setPhoneNumber(userRegisterRequestDto.getPhone());
            if (userRepository.countByUserProfilePhoneNumber(userRegisterRequestDto.getPhone()) > 0) {
                throw new BadParameterValueException("Registration failed! A user with this phone already exists!");
            }
        }
        LOGGER.info("Check login");
        if (userRepository.countByLogin(userRegisterRequestDto.getLogin()) > 0) {
            throw new BadParameterValueException("Registration failed! A user with this login already exists!");
        }
        String password = user.getPassword();
        LOGGER.info("Encrypt password");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        userProfileRepository.save(user.getUserProfile());
        UserEntity savedUser = userRepository.save(user);
        Catalog personalCatalog = new Catalog();
        personalCatalog.setNameEn("Personal catalog");
        personalCatalog.setUser(savedUser);
        catalogRepository.save(personalCatalog);
        userRegisterResponseDto.setCode(201);
        userRegisterResponseDto.setStatus("OK");
        userRegisterResponseDto.setMessage("Registration success!");

        return userRegisterResponseDto;
    }

    @Override
    public ApplicationResponseDto<?> userLogout(Long userId, boolean status) {
        ApplicationResponseDto<?> userRegisterResponseDto = new ApplicationResponseDto<>();
        userRegisterResponseDto.setCode(403);
        userRegisterResponseDto.setStatus("ERROR");
        if (userId != null) {
            LOGGER.info("Find user with user id = " + userId);
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with user id = " + userId + " not found when userLogout execute!"));
            user.setAuthorisationStatus(status);
            user.setLastSignIn(new Timestamp(System.currentTimeMillis()));
            LOGGER.info("Set auth status and last sign in with user id = " + userId);
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
        LOGGER.info("Check phone");
        if (userLoginRequestDto.getPhone() != null) {
            user = userRepository.findByUserProfilePhoneNumber(userLoginRequestDto.getPhone()).orElseThrow(() -> new NotFoundException("Authorization failed with phone = " + userLoginRequestDto.getPhone()));
        }
        LOGGER.info("Check email");
        if (userLoginRequestDto.getEmail() != null) {
            user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(() -> new NotFoundException("Authorization failed with phone = " + userLoginRequestDto.getEmail()));
        }
        if (user == null) {
            throw new BadParameterValueException("User not found, not enough parameters");
        }
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new NotFoundException("Invalid password error!");
        }
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        LOGGER.info("Set auth status for user with user id = " + user.getId());
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
        LOGGER.info("Find user by id = " + userId);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found when findUserById execute"));
        LOGGER.info("Find catalogs by user id = " + userId);
        List<Catalog> catalogs = catalogRepository.findByUserId(user.getId());
        UserProfileResponseDto userProfileResponseDto = userProfileMapper.userEntityToUserProfileResponseDto(user);
        List<Long> catalogIdsList = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            catalogIdsList.add(catalog.getId());
        }
        LOGGER.info("Set imageUrl, subscribed flag and catalogsIdsList for user with id = " + userId);
        userProfileResponseDto.setUserImageUrl(user.getUserProfile().getUserImageUrl());
        userProfileResponseDto.setSubscribed(subscriptionRepository.existsBySubscriberIdAndSellerId(userId, id));
        userProfileResponseDto.setCatalogsIdList(catalogIdsList);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("User found!");
        responseDto.setPayload(userProfileResponseDto);
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<UserProfileResponseDto> saveUserProfileById(Long id, UserProfileRequestDto requestDto) throws NotFoundException {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find user by id = " + id);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id = " + id + " when saveUserProfileById execute"));
        if (userRepository.countByLogin(requestDto.getLogin()) > 0) {
            if (user.getLogin() == null || !user.getLogin().equals(requestDto.getLogin()))
                throw new BadParameterValueException("Update failed! A user with this login already exists!");
        }
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
        if (requestDto.getGender().isEmpty()) {
            userProfile.setGender(null);
        } else {
            //userProfile.setGender(UserGender.valueOf(requestDto.getGender()));
        }
        userProfile.setPostalCode(requestDto.getPostalCode());
        if (requestDto.getImageData() != null && !requestDto.getImageData().isEmpty()) {
            String carg = requestDto.getImageData();
            carg = carg.replace("\n", "");
            try {
                LOGGER.info("Upload image to cloudinary");
                Map cloudinaryMap = cloudinary.uploader().upload("data:image/png;base64," + carg,
                        ObjectUtils.emptyMap());
                String secureUrl = (String) cloudinaryMap.get("secure_url");
                LOGGER.info("Image uploaded with url = " + secureUrl);
                userProfile.setUserImageUrl(secureUrl);
            } catch (IOException e) {
                throw new BadParameterValueException("Image broke)");
            }
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
        LOGGER.info("Save user info by user id = " + id);
        user = userRepository.save(user);

        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("User profile update!");
        responseDto.setPayload(userProfileMapper.userEntityToUserProfileResponseDto(user));
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> deleteUserById(Long id) throws NotFoundException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find user by id = " + id);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id = " + id + " not found when deleteUserById execute"));
        LOGGER.info("Delete user with id = " + id);
        userProfileRepository.delete(user.getUserProfile());
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("User delete!");
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Long> userGoogleAuth(UserGoogleAuthDto dto) {
        LOGGER.info("Find user by email = " + dto.getEmail());
        Optional<UserEntity> user = userRepository.findByEmail(dto.getEmail());
        ApplicationResponseDto<Long> userLoginResponseDto = new ApplicationResponseDto<>();
        if (user.isEmpty()) {
            LOGGER.info("User with email = " + dto.getEmail() + " found");
            UserEntity userToSave = new UserEntity();
            userToSave.setEmail(dto.getEmail());
            UserProfile profile = new UserProfile();
            profile.setFirstname(dto.getFirstname());
            profile.setLastname(dto.getLastname());
            userToSave.setUserProfile(profile);
            userProfileRepository.save(profile);
            userToSave = userRepository.save(userToSave);
            Catalog personalCatalog = new Catalog();
            personalCatalog.setNameEn("Personal catalog");
            personalCatalog.setUser(userToSave);
            LOGGER.info("Save user with email = " + dto.getEmail());
            catalogRepository.save(personalCatalog);
            userLoginResponseDto.setPayload(userToSave.getId());
        } else {
            LOGGER.info("User with email = " + dto.getEmail() + " not found");
            userLoginResponseDto.setPayload(user.get().getId());
        }
        userLoginResponseDto.setCode(200);
        userLoginResponseDto.setStatus("OK");
        userLoginResponseDto.setMessage("Authorization success!");
        return userLoginResponseDto;
    }
}
