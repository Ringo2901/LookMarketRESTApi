package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.dto.user.UserRegisterRequestDto;
import by.bsuir.lookmanager.services.UserService;
import by.bsuir.lookmanager.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signIn")
    public ResponseEntity<ApplicationResponseDto<?>> userLogin(@RequestBody UserLoginRequestDto requestDto) {
        ApplicationResponseDto<Long> responseDto = userService.userLogin(requestDto);
        if (responseDto.getPayload()!=null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, jwtProvider.createToken(responseDto.getPayload()));
            return ResponseEntity.status(responseDto.getCode())
                    .headers(headers)
                    .body(responseDto);
        } else{
            return ResponseEntity.status(responseDto.getCode())
                    .body(responseDto);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<ApplicationResponseDto<?>> userRegister(@RequestBody UserRegisterRequestDto requestDto) {
        ApplicationResponseDto<?> responseDto = userService.userRegister(requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
