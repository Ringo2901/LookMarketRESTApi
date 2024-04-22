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
import com.moesif.api.models.UserBuilder;
import com.moesif.api.models.UserModel;
import com.moesif.servlet.MoesifFilter;
import jakarta.servlet.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    @Autowired
    private Filter moesifFilter;
    private static final Logger LOGGER = LogManager.getLogger(SubscriptionServiceImpl.class);

    @Override
    public ApplicationResponseDto<List<UserSubscriberResponseDto>> getSubscriptions(Long userId) throws NotFoundException {
        ApplicationResponseDto<List<UserSubscriberResponseDto>> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find user by id = " + userId);
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            responseDto.setStatus("OK");
            responseDto.setCode(200);
            responseDto.setMessage("User's subscriptions found!");
            List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllBySubscriberId(userId);
            List<UserEntity> userEntities = new ArrayList<>();
            for (SubscriptionEntity entity : subscriptionEntities) {
                LOGGER.info("Found subscriber by user id = " + userId + " by seller with id = " + entity.getSellerId());
                userEntities.add(userRepository.findById(entity.getSellerId()).orElse(null));
            }
            responseDto.setPayload(userSubscribersListMapper.toSubscribersList(userEntities));
        } else {
            throw new NotFoundException("User with id = " + userId + " not found when getSubscriptions execute");
        }
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<?> subscribe(Long subscriberId, Long sellerId) throws NotFoundException, AlreadyExistsException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new NotFoundException("Subscriber with id = " + subscriberId + " not found when subscribe execute!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new NotFoundException("Seller with id = " + sellerId + " not found when subscribe execute!");
        }
        if (subscriptionRepository.existsBySubscriberIdAndSellerId(subscriberId, sellerId)) {
            throw new AlreadyExistsException("User with id = " + subscriberId + " already subscribe for seller with id = " + sellerId);
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setSubscriberId(subscriberId);
        subscriptionEntity.setSellerId(sellerId);
        LOGGER.info("Save new subscription with subscriber id = " + subscriberId + " and seller id = " + sellerId);
        subscriptionRepository.save(subscriptionEntity);

        UserEntity user = userRepository.findById(subscriberId).orElseThrow(() -> new NotFoundException("User not found!"));
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        responseDto.setMessage("Subscribed successfully!");
        responseDto.setStatus("OK");
        responseDto.setCode(201);
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<?> unsubscribe(Long subscriberId, Long sellerId) throws NotFoundException, AlreadyExistsException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        if (!userRepository.existsById(subscriberId)) {
            throw new NotFoundException("Subscriber with id = " + subscriberId + " not found when unsubscribe execute!");
        }
        if (!userRepository.existsById(sellerId)) {
            throw new NotFoundException("Seller with id = " + sellerId + " not found when unsubscribe execute!");
        }
        LOGGER.info("Delete subscription with subscriber id = " + subscriberId + " and seller id = " + sellerId);
        subscriptionRepository.delete(subscriptionRepository.findBySubscriberIdAndSellerId(subscriberId, sellerId).orElseThrow(() -> new AlreadyExistsException("Not subscribed!")));

        UserEntity user = userRepository.findById(subscriberId).orElseThrow(() -> new NotFoundException("User not found!"));
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        responseDto.setMessage("Unsubscribed successfully!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }
}
