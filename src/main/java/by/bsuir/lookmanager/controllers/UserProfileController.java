package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.UserService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token,
                                                                                         @RequestParam(required = false, defaultValue = "en") String lang) {
        if (token == null){
            throw new NotFoundException("User not found!");
        }
        LOGGER.info("Start getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(0L, jwtValidator.validateTokenAndGetUserId(token), lang);
        LOGGER.info("Finish getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/isCurrent/{id}")
    public ResponseEntity<ApplicationResponseDto<Boolean>> isCurrentUser(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @PathVariable Long id) {
        LOGGER.info("Start getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)));
        ApplicationResponseDto<Boolean> responseDto = new ApplicationResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("");
        responseDto.setStatus("OK");
        responseDto.setPayload(Objects.equals(id, jwtValidator.validateTokenAndGetUserId(token.orElse(null))));
        LOGGER.info("Finish getting current user with id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getUserById(@RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                      @RequestParam(required = false, defaultValue = "en") String lang,
                                                                                      @PathVariable Long id) {
        LOGGER.info("Start getting user with id = " + id);
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), id, lang);
        LOGGER.info("Finish getting user with id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping()
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> updateUserProfileById(@RequestHeader("Authorization") String token,
                                                                                                @RequestParam(required = false, defaultValue = "en") String lang,
                                                                                                @RequestBody UserProfileRequestDto requestDto) {
        LOGGER.info("Start updating user with id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.saveUserProfileById(jwtValidator.validateTokenAndGetUserId(token), requestDto, lang);
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
