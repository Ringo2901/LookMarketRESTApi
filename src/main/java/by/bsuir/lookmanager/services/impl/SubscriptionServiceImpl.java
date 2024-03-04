package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.dto.user.mapper.UserSubscribersListMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscribersListMapper userSubscribersListMapper;

    @Override
    public ApplicationResponseDto<List<UserSubscriberResponseDto>> getSubscriptions(Long userId) {
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            responseDto.setStatus("OK");
            responseDto.setCode(200);
            responseDto.setMessage("User's subscriptions found!");
            responseDto.setPayload(userSubscribersListMapper.toSubscribersList(userRepository.findBySubscriptionsSubscriberId(userId)));
        } else {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("User not found!");
        }
        return responseDto;
    }
}
