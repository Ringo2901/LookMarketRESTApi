package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SizeRepository extends JpaRepository<ProductSize, Long>, JpaSpecificationExecutor<ProductSize> {
}
