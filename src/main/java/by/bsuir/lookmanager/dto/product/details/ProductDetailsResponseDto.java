package by.bsuir.lookmanager.dto.product.details;

import by.bsuir.lookmanager.entities.product.information.AgeType;
import by.bsuir.lookmanager.entities.product.information.Condition;
import by.bsuir.lookmanager.entities.product.information.ProductGender;
import by.bsuir.lookmanager.entities.product.information.ProductStatus;
import by.bsuir.lookmanager.entities.product.promotion.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsResponseDto {
    private Long id;
    private String title;
    private String createdTime;
    private String updateTime;
    private Double price;
    private ProductStatus status;

    private String description;
    private String gender;
    private String season;
    private String condition;
    private String ageType;
    private String brandName;

    private Double latitude;
    private Double longitude;
    private Integer viewNumber;

    private List<Integer> sizes;
    private List<String> colors;
    private List<String> materials;
    private List<String> tags;

    private Promotion promotion;
    private String subCategoryName;
    private String categoryName;

    private String firstname;
    private String lastname;
    private String login;
    private Long userId;
    private String userImageUrl;

    private List<String> imagesUrls;

    private boolean isFavourite;
}
