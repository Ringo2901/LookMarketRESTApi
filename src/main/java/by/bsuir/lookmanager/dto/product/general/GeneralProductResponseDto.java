package by.bsuir.lookmanager.dto.product.general;

import by.bsuir.lookmanager.enums.ProductStatus;
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
    private ProductStatus status;
    private String createdTime;

    private String updateTime;
    private Double price;
    private String subCategoryName;
    private String categoryName;

    private String login;
    private Long userId;
    private String userImageUrl;

    private Long imageId;
    private String imageUrl;

    private boolean isFavourite;
}
