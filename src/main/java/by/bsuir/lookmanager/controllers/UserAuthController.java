package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserGoogleAuthDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.services.UserService;
import by.bsuir.lookmanager.utils.JwtProvider;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class UserAuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(UserAuthController.class);

    @PostMapping("/signIn")
    public ResponseEntity<ApplicationResponseDto<?>> userLogin(@RequestBody UserLoginRequestDto requestDto) {
        LOGGER.info("Start logging with request = " + requestDto);
        ApplicationResponseDto<Long> responseDto = userService.userLogin(requestDto);
        LOGGER.info("Finish logging with response = " + responseDto);
        return getApplicationResponseDtoResponseEntity(responseDto);
    }

    @GetMapping("/setStatus")
    public ResponseEntity<ApplicationResponseDto<?>> userSetStatus(@RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                   @RequestParam boolean status) {
        LOGGER.info("Start logout with authorisation status = " + status);
        ApplicationResponseDto<?> responseDto = userService.userLogout(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), status);
        LOGGER.info("Finish logout with authorisation status = " + status);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/google")
    public ResponseEntity<ApplicationResponseDto<?>> userGoogleAuth(@RequestBody UserGoogleAuthDto requestDto) {
        LOGGER.info("Start google auth with request = " + requestDto);
        ApplicationResponseDto<Long> responseDto = userService.userGoogleAuth(requestDto);
        LOGGER.info("Finish google auth with request = " + responseDto);
        return getApplicationResponseDtoResponseEntity(responseDto);
    }

    private ResponseEntity<ApplicationResponseDto<?>> getApplicationResponseDtoResponseEntity(ApplicationResponseDto<Long> responseDto) {
        if (responseDto.getPayload() != null) {
            HttpHeaders headers = new HttpHeaders();
            LOGGER.info("Auth token set");
            headers.add(HttpHeaders.AUTHORIZATION, jwtProvider.createToken(responseDto.getPayload()));
            return ResponseEntity.status(responseDto.getCode())
                    .headers(headers)
                    .body(responseDto);
        } else {
            LOGGER.info("Auth token not set");
            return ResponseEntity.status(responseDto.getCode())
                    .body(responseDto);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<ApplicationResponseDto<?>> userRegister(@RequestBody UserRegisterRequestDto requestDto) {
        LOGGER.info("Start registration with request = " + requestDto);
        ApplicationResponseDto<?> responseDto = userService.userRegister(requestDto);
        LOGGER.info("Finish registration with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
