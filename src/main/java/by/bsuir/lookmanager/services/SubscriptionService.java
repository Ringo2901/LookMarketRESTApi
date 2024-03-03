package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;

import java.util.List;

public interface SubscriptionService {
    ApplicationResponseDto<List<UserSubscriberResponseDto>> getSubscriptions (Long userId);
}
