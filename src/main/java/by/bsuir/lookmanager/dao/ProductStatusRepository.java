package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long>, JpaSpecificationExecutor<ProductStatus> {
}

