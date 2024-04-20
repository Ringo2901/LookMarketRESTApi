package by.bsuir.lookmanager.dao.specification.impl;

import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.Category;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationImpl implements ProductSpecification {
    @Override
    public Specification<ProductEntity> byCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductEntity, SubCategory> subCategoryJoin = root.join("subCategory");
            Join<SubCategory, Category> categoryJoin = subCategoryJoin.join("category");
            return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
        };
    }

    @Override
    public Specification<ProductEntity> byCatalogId(Long catalogId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("catalog").get("id"), catalogId);
    }
}
