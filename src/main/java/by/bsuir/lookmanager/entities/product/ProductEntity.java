package by.bsuir.lookmanager.entities.product;

import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.sql.Timestamp;

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
    @Column(name = "created_time", insertable = false)
    private Timestamp createdTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProductStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_information_id")
    private ProductInformation productInformation;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @OneToOne (cascade=CascadeType.ALL)
    @JoinColumn(name="promotion_id")
    private Promotion promotion;
}
