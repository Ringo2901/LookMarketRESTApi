package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginAndPassword (String login, String password);
    int countByEmail (String email);
    int countByLogin (String login);
    List<UserEntity> findBySubscriptionsSubscriberId(Long userId);
}
