package by.bsuir.lookmanager.dto.product;

import by.bsuir.lookmanager.entities.product.information.ProductInformation;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import by.bsuir.lookmanager.entities.product.promotion.Promotion;
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
    private ProductInformation productInformation;
    private Promotion promotion;
    private SubCategory subCategory;
}
