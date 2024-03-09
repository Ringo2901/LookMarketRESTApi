package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.SubscriptionRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.dto.user.mapper.UserSubscribersListMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.SubscriptionEntity;
import by.bsuir.lookmanager.exceptions.AlreadyExistsException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
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
    public ApplicationResponseDto<List<UserSubscriberResponseDto>> getSubscriptions(Long userId) throws NotFoundException {
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
            throw new NotFoundException("User not found!");
        }
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<?> subscribe(Long subscriberId, Long sellerId) throws NotFoundException, AlreadyExistsException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new NotFoundException("Subscriber not found!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new NotFoundException("Seller not found!");
        }
        if (subscriptionRepository.existsBySubscriberIdAndSellerId(subscriberId, sellerId)) {
            throw new AlreadyExistsException("Already subscribe");
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
    public ApplicationResponseDto<?> unsubscribe(Long subscriberId, Long sellerId) throws NotFoundException, AlreadyExistsException{
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new NotFoundException("Subscriber not found!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new NotFoundException("Seller not found!");
        }

        subscriptionRepository.delete(subscriptionRepository.findBySubscriberIdAndSellerId(subscriberId, sellerId).orElseThrow(() -> new AlreadyExistsException("Not subscribed!")));

        responseDto.setMessage("Unsubscribed successfully!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }
}
