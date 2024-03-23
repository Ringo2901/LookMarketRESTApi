package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.user.UserSubscriberResponseDto;
import by.bsuir.lookmanager.entities.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndPassword (String email, String password);
    Optional<UserEntity> findByUserProfilePhoneNumberAndPassword (String phoneNumber, String password);
    Optional<UserEntity> findByEmail (String email);
    int countByEmail (String email);
    int countByUserProfilePhoneNumber (String phoneNumber);
    int countByLogin (String login);
}
