package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginAndPassword (String login, String password);
    int countByEmail (String email);
    int countByLogin (String login);
}
