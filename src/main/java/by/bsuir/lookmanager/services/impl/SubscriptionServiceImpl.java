package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.SubscriptionRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.dto.user.mapper.UserSubscribersListMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.SubscriptionEntity;
import by.bsuir.lookmanager.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscribersListMapper userSubscribersListMapper;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public ApplicationResponseDto<List<UserSubscriberResponseDto>> getSubscriptions(Long userId) {
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            responseDto.setStatus("OK");
            responseDto.setCode(200);
            responseDto.setMessage("User's subscriptions found!");
            List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllBySubscriberId(userId);
            List<UserEntity> userEntities = new ArrayList<>();
            for (SubscriptionEntity entity: subscriptionEntities){
                userEntities.add(userRepository.findById(entity.getSellerId()).orElse(null));
            }
            responseDto.setPayload(userSubscribersListMapper.toSubscribersList(userEntities));
        } else {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("User not found!");
        }
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<?> subscribe(Long subscriberId, Long sellerId) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new RuntimeException("Subscriber not found!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new RuntimeException("Seller not found!");
        }
        if (subscriptionRepository.existsBySubscriberIdAndSellerId(subscriberId, sellerId)) {
            throw new RuntimeException("Already subscribe");
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setSubscriberId(subscriberId);
        subscriptionEntity.setSellerId(sellerId);

        subscriptionRepository.save(subscriptionEntity);

        responseDto.setMessage("Subscribed successfully!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<?> unsubscribe(Long subscriberId, Long sellerId) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new RuntimeException("Subscriber not found!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new RuntimeException("Seller not found!");
        }

        subscriptionRepository.delete(subscriptionRepository.findBySubscriberIdAndSellerId(subscriberId, sellerId).orElseThrow(() -> new RuntimeException("Not subscribed!")));

        responseDto.setMessage("Unsubscribed successfully!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }
}
