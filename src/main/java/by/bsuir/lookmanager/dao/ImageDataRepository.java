package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ImageDataRepository extends JpaRepository<ImageData, Long>, JpaSpecificationExecutor<ImageData> {
    List<ImageData> findByProductId(Long id);
    ImageData findFirstByProductId(Long id);
}
