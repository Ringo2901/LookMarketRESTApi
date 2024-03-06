package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/subscriptions")
public class SubscribersController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<UserSubscriberResponseDto>>> getAllSubscriptions(@PathVariable Long id) {
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = subscriptionService.getSubscriptions(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{subscriberId}/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> subscribe(@PathVariable Long subscriberId, @PathVariable Long sellerId) {
        ApplicationResponseDto<?> responseDto = subscriptionService.subscribe(subscriberId, sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{subscriberId}/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> unsubscribe(@PathVariable Long subscriberId, @PathVariable Long sellerId) {
        ApplicationResponseDto<?> responseDto = subscriptionService.unsubscribe(subscriberId, sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
