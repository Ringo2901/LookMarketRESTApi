package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.services.SubscriptionService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/subscriptions")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class SubscribersController {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(SubscribersController.class);

    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<UserSubscriberResponseDto>>> getAllSubscriptions(@RequestHeader(value = "Authorization", required = false) Optional<String> token) {
        LOGGER.info("Start getting all subscribers for user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)));
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = subscriptionService.getSubscriptions(jwtValidator.validateTokenAndGetUserId(token.orElse(null)));
        LOGGER.info("Finish getting all subscribers for user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> subscribe(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @PathVariable Long sellerId) {
        LOGGER.info("Start subscribe with user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)) + " and seller id = " + sellerId);
        ApplicationResponseDto<?> responseDto = subscriptionService.subscribe(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), sellerId);
        LOGGER.info("Finish subscribe with user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)) + " and seller id = " + sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> unsubscribe(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @PathVariable Long sellerId) {
        LOGGER.info("Start unsubscribe with user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)) + " and seller id = " + sellerId);
        ApplicationResponseDto<?> responseDto = subscriptionService.unsubscribe(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), sellerId);
        LOGGER.info("Finish unsubscribe with user id = " + jwtValidator.validateTokenAndGetUserId(token.orElse(null)) + " and seller id = " + sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
