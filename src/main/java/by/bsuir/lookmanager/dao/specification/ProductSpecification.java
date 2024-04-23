package by.bsuir.lookmanager.dao.specification;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecification {
    Specification<ProductEntity> byCategoryId(String sex, String userId);
    Specification<ProductEntity> byCatalogId(Long catalogId);

    Specification<ProductEntity> byUserId(Long userId);
}
