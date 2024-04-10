package by.bsuir.lookmanager.dto.configuration;

import by.bsuir.lookmanager.entities.product.information.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationResponseDto {
    private List<ProductBrand> productBrands;
    private List<ProductSize> productSizes;
    private List<ProductColor> productColors;
    private List<ProductTag> productTags;
    private List<ProductMaterial> productMaterials;
    private List<Category> categories;
    private List<SubCategoryDto> subCategories;
    private List<String> seasons;
    private List<String> conditions;
    private List<String> ageTypes;
    private List<String> productGenders;
    private List<String> userGenders;

}
