package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<ApplicationResponseDto<Object>> userLogin(@RequestBody UserLoginRequestDto requestDto) {
        ApplicationResponseDto<Object> responseDto = userService.userLogin(requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/signUp")
    public ResponseEntity<ApplicationResponseDto<Object>> userRegister(@RequestBody UserRegisterRequestDto requestDto) {
        ApplicationResponseDto<Object> responseDto = userService.userRegister(requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
