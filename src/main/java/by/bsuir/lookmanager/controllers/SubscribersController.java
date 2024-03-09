package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.services.SubscriptionService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/subscriptions")
public class SubscribersController {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private JwtValidator jwtValidator;

    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<UserSubscriberResponseDto>>> getAllSubscriptions(@RequestHeader("Authorization") String token) {
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = subscriptionService.getSubscriptions(jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> subscribe(@RequestHeader("Authorization") String token, @PathVariable Long sellerId) {
        ApplicationResponseDto<?> responseDto = subscriptionService.subscribe(jwtValidator.validateTokenAndGetUserId(token), sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> unsubscribe(@RequestHeader("Authorization") String token, @PathVariable Long sellerId) {
        ApplicationResponseDto<?> responseDto = subscriptionService.unsubscribe(jwtValidator.validateTokenAndGetUserId(token), sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
