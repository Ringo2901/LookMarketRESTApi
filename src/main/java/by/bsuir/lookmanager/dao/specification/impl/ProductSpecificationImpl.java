package by.bsuir.lookmanager.dao.specification.impl;

import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.product.information.ProductGender;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationImpl implements ProductSpecification {
    @Override
    public Specification<ProductEntity> byCategoryId(String sex, String userId) {
        return Specification.where(byGender(sex)).and(byUserNotId(userId));
    }

    private Specification<ProductEntity> byGender(String sex) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductEntity, ProductInformation> productInfoJoin = root.join("productInformation");

            return criteriaBuilder.equal(productInfoJoin.get("gender"), sex);
        };
    }

    private Specification<ProductEntity> byUserNotId(String userId) {
        return (root, query, criteriaBuilder) -> {
            Join<ProductEntity, Catalog> catalogJoin = root.join("catalog", JoinType.INNER);
            Join<Catalog, UserEntity> userJoin = catalogJoin.join("user", JoinType.INNER);

            return criteriaBuilder.notEqual(userJoin.get("id"), userId);
        };
    }

    @Override
    public Specification<ProductEntity> byCatalogId(Long catalogId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("catalog").get("id"), catalogId);
    }

    @Override
    public Specification<ProductEntity> byUserId(Long userId) {
        return Specification.where(byUserNotId(userId.toString()));
    }
}
