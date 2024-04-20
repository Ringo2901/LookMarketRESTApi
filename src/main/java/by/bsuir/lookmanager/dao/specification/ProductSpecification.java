package by.bsuir.lookmanager.dao.specification;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecification {
    Specification<ProductEntity> byCategoryId(Long categoryId);
    Specification<ProductEntity> byCatalogId(Long catalogId);
}
