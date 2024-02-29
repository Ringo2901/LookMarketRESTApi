package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
    List<Media> findByProductId(Long productId);
    Media findFirstByProductId (Long productId);
}
