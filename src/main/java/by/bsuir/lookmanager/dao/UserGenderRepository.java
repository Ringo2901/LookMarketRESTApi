package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.UserGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserGenderRepository extends JpaRepository<UserGender, Long>, JpaSpecificationExecutor<UserGender> {
}

