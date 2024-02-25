package by.bsuir.lookmanager.entities.product;

import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "created_time")
    private Timestamp createdTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column(name = "price")
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;
    @Column(name = "promotion")
    private boolean hasPromotion;
    @Column(name = "promotion_duration_to")
    private Timestamp promotionDurationTo;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_information_id")
    private ProductInformation productInformation;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
}
