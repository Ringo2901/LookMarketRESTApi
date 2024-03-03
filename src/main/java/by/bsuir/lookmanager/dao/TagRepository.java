package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagRepository extends JpaRepository<ProductTag, Long>, JpaSpecificationExecutor<ProductTag> {
}
