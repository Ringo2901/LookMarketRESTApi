package by.bsuir.lookmanager.dto.product.general;

import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import by.bsuir.lookmanager.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralProductResponseDto {
    private String title;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Double price;
    private ProductStatus status;
    private Promotion promotion;
    private String subCategoryName;
    private String categoryName;
}
