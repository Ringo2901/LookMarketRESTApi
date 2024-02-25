package by.bsuir.lookmanager.dto.product;

import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String title;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Double price;
    private ProductStatus status;
    private boolean hasPromotion;
    private Timestamp promotionDurationTo;
    private ProductInformation productInformation;
}
