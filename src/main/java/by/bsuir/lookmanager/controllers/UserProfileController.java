package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
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
}
