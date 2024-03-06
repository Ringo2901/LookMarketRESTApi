package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileRequestDto;
import by.bsuir.lookmanager.dto.user.UserProfileResponseDto;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> getUserById(@PathVariable Long id) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.findUserById(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<UserProfileResponseDto>> updateUserProfileById(@PathVariable Long id, @RequestBody UserProfileRequestDto requestDto) {
        ApplicationResponseDto<UserProfileResponseDto> responseDto = userService.saveUserProfileById(id, requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteUserById(@PathVariable Long id) {
        ApplicationResponseDto<?> responseDto = userService.deleteUserById(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
