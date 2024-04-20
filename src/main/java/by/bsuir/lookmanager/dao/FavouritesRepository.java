package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.information.FavouritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FavouritesRepository extends JpaRepository<FavouritesEntity, Long>, JpaSpecificationExecutor<FavouritesEntity> {
    boolean existsByUserIdAndProductId (Long userId, Long productId);
    List<FavouritesEntity> findFirst2ByUserId (Long userId);
}
