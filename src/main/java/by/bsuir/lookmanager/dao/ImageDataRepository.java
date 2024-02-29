package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageDataRepository extends JpaRepository<ImageData, Long>, JpaSpecificationExecutor<ImageData> {
    @Query("SELECT imageData FROM ImageData imageData WHERE imageData.id IN :ids")
    List<ImageData> findByIdIn(@Param("ids") List<Long> ids);
}
