package by.bsuir.lookmanager.dto.product.details;

import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.Condition;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsRequestDto {
    private String title;
    private Double price;

    private String description;
    private ProductGender gender;
    private Season season;
    private Condition condition;
    private AgeType ageType;
    private Long brandId;

    private List<Long> sizesId;
    private List<Long> colorsId;
    private List<Long> materialsId;
    private List<Long> tagsId;

    private Long subCategoryId;

    private Long catalogId;
}