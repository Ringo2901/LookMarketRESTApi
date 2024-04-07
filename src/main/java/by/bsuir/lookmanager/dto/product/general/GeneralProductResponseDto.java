package by.bsuir.lookmanager.dto.product.general;

import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import by.bsuir.lookmanager.enums.ProductStatus;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralProductResponseDto {
    private Long id;
    private String title;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Double price;
    private String subCategoryName;
    private String categoryName;

    private String login;
    private Long userId;

    private Long imageId;
    private String imageData;
    //private boolean isFavourite;
}
