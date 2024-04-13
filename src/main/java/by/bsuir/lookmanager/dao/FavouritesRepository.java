package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.FavouritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FavouritesRepository extends JpaRepository<FavouritesEntity, Long>, JpaSpecificationExecutor<FavouritesEntity> {
    boolean existsByUserIdAndProductId (Long userId, Long productId);
}
