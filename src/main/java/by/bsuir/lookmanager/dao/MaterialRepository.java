package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaterialRepository extends JpaRepository<ProductMaterial, Long>, JpaSpecificationExecutor<ProductMaterial> {
}
