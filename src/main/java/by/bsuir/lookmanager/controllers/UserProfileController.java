package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.services.UserService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class UserProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(UserProfileController.class);
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getCurrentUser(@RequestHeader("Authorization") String token) {
        LOGGER.info("Start getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(0L, jwtValidator.validateTokenAndGetUserId(token));
        LOGGER.info("Finish getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getUserById(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @PathVariable Long id) {
        LOGGER.info("Start getting user with id = " + id);
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), id);
        LOGGER.info("Finish getting user with id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping()
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> updateUserProfileById(@RequestHeader("Authorization") String token, @RequestBody UserProfileRequestDto requestDto) {
        LOGGER.info("Start updating user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.saveUserProfileById(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        LOGGER.info("Finish updating user response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping()
    public ResponseEntity<ApplicationResponseDto<?>> deleteUserById(@RequestHeader("Authorization") String token) {
        LOGGER.info("Start deleting user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<?> responseDto = userService.deleteUserById(jwtValidator.validateTokenAndGetUserId(token));
        LOGGER.info("Finish deleting user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
