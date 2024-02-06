package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApplicationResponseDto<Object> userLogin(@RequestBody UserLoginRequestDto requestDto) {
        return userService.userLogin(requestDto);
    }

    @PostMapping("/signUp")
    public ApplicationResponseDto<Object> userRegister(@RequestBody UserRegisterRequestDto requestDto) {
        return userService.userRegister(requestDto);
    }
}
