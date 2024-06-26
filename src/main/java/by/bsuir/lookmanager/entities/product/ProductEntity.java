package by.bsuir.lookmanager.entities.product;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.information.*;
import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "created_time")
    private Timestamp createdTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProductStatus status;
    @OneToOne
    @JoinColumn(name = "product_information_id")
    private ProductInformation productInformation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @OneToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public String toText(Map<String, Integer> importanceCoefficients) {
        StringBuilder textBuilder = new StringBuilder();

        if (title != null) {
            for (int i = 0; i < importanceCoefficients.get("title"); i++) {
                textBuilder.append(title.toLowerCase()).append(" ");
            }
        }

        if (subCategory != null) {
            for (int i = 0; i < importanceCoefficients.get("subCategory"); i++) {
                textBuilder.append(subCategory.getName().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getGender() != null) {
            for (int i = 0; i < importanceCoefficients.get("gender"); i++) {
                textBuilder.append(productInformation.getGender().toString().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getDescription() != null) {
            for (int i = 0; i < importanceCoefficients.get("description"); i++) {
                textBuilder.append(productInformation.getDescription().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getSeason() != null) {
            for (int i = 0; i < importanceCoefficients.get("season"); i++) {
                textBuilder.append(productInformation.getSeason().toString().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getCondition() != null) {
            for (int i = 0; i < importanceCoefficients.get("condition"); i++) {
                textBuilder.append(productInformation.getCondition().toString().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getAgeType() != null) {
            for (int i = 0; i < importanceCoefficients.get("ageType"); i++) {
                textBuilder.append(productInformation.getAgeType().toString().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getProductBrand() != null) {
            for (int i = 0; i < importanceCoefficients.get("productBrand"); i++) {
                textBuilder.append(productInformation.getProductBrand().getBrandName().toLowerCase()).append(" ");
            }
        }

        if (productInformation.getSizes() != null) {
            for (int i = 0; i < importanceCoefficients.get("size"); i++) {
                for (ProductSize size : productInformation.getSizes()) {
                    textBuilder.append(size.getSize()).append(" ");
                }
            }
        }

        if (productInformation.getColors() != null) {
            for (int i = 0; i < importanceCoefficients.get("color"); i++) {
                for (ProductColor size : productInformation.getColors()) {
                    textBuilder.append(size.getName().toLowerCase()).append(" ");
                }
            }
        }

        if (productInformation.getMaterials() != null) {
            for (int i = 0; i < importanceCoefficients.get("material"); i++) {
                for (ProductMaterial size : productInformation.getMaterials()) {
                    textBuilder.append(size.getMaterialName().toLowerCase()).append(" ");
                }
            }
        }

        if (productInformation.getTags() != null) {
            for (int i = 0; i < importanceCoefficients.get("tag"); i++) {
                for (ProductTag size : productInformation.getTags()) {
                    textBuilder.append(size.getTagName().toLowerCase()).append(" ");
                }
            }
        }

        return textBuilder.toString();
    }
}
