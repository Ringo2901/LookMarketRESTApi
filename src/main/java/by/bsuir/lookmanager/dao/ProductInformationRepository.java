package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductInformationRepository extends JpaRepository<ProductInformation, Long>, JpaSpecificationExecutor<ProductInformation> {
}
