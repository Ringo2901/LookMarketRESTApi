package by.bsuir.lookmanager.dto.configuration;

import by.bsuir.lookmanager.entities.product.information.*;
import by.bsuir.lookmanager.entities.user.information.UserGender;
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
    private List<EnumDto> productColors;
    private List<EnumDto> productTags;
    private List<EnumDto> productMaterials;
    private List<EnumDto> categories;
    private List<SubCategoryDto> subCategories;
    private List<EnumDto> seasons;
    private List<EnumDto> conditions;
    private List<EnumDto> ageTypes;
    private List<EnumDto> productGenders;
    private List<EnumDto> userGenders;

    private List<CountryWithCityDto> countryWithCityDtos;
}
