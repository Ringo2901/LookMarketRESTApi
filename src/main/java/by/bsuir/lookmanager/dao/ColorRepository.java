package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColorRepository extends JpaRepository<ProductColor, Long>, JpaSpecificationExecutor<ProductColor> {
}
