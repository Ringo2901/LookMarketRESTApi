package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductGenderRepository extends JpaRepository<ProductGender, Long>, JpaSpecificationExecutor<ProductGender> {
}

