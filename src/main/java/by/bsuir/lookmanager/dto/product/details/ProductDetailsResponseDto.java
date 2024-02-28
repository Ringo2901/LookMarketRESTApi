package by.bsuir.lookmanager.dto.product.details;

import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import by.bsuir.lookmanager.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsResponseDto {
    private String title;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Double price;
    private ProductStatus status;

    private ProductGender gender;
    private Season season;
    private Condition condition;
    private AgeType ageType;
    private String brandName;

    private List<Integer> sizes;
    private List<String> colors;
    private List<String> materials;
    private List<String> tags;

    private Promotion promotion;
    private String subCategoryName;
    private String categoryName;
}
