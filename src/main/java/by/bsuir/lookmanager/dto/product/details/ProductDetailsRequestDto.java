package by.bsuir.lookmanager.dto.product.details;

import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.Condition;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsRequestDto {
    @NotBlank
    @Size(min = 5, max = 100)
    private String title;
    @NotBlank
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

    @NotBlank
    private Long catalogId;
}
