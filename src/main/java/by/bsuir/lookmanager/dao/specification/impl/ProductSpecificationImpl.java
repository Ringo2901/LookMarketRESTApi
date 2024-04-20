package by.bsuir.lookmanager.dao.specification.impl;

import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.Category;
import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import by.bsuir.lookmanager.enums.ProductGender;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationImpl implements ProductSpecification {
    @Override
    public Specification<ProductEntity> byCategoryId(String sex) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductEntity, ProductInformation> productInfoJoin = root.join("productInformation");

            return criteriaBuilder.equal(productInfoJoin.get("gender"), ProductGender.valueOf(sex));
        };
    }


    @Override
    public Specification<ProductEntity> byCatalogId(Long catalogId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("catalog").get("id"), catalogId);
    }
}
