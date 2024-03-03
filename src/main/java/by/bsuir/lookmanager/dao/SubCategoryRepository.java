package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductBrand;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>, JpaSpecificationExecutor<SubCategory> {
}
