package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.services.UserService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtValidator jwtValidator;
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getCurrentUser(@RequestHeader("Authorization") String token) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getUserById(@PathVariable Long id) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping()
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> updateUserProfileById(@RequestHeader("Authorization") String token, @RequestBody UserProfileRequestDto requestDto) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.saveUserProfileById(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping()
    public ResponseEntity<ApplicationResponseDto<?>> deleteUserById(@RequestHeader("Authorization") String token) {
        ApplicationResponseDto<?> responseDto = userService.deleteUserById(jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
