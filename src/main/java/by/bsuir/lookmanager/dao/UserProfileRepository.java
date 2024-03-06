package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
}
