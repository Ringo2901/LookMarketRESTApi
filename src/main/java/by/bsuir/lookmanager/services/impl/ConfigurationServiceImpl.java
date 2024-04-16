package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.configuration.CityDto;
import by.bsuir.lookmanager.dto.configuration.ConfigurationResponseDto;
import by.bsuir.lookmanager.dto.configuration.CountryWithCityDto;
import by.bsuir.lookmanager.dto.configuration.SubCategoryDto;
import by.bsuir.lookmanager.entities.product.information.SubCategory;
import by.bsuir.lookmanager.entities.user.information.City;
import by.bsuir.lookmanager.entities.user.information.Country;
import by.bsuir.lookmanager.enums.*;
import by.bsuir.lookmanager.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public ApplicationResponseDto<ConfigurationResponseDto> getConfiguration() {
        ApplicationResponseDto<ConfigurationResponseDto> responseDto = new ApplicationResponseDto<>();
        ConfigurationResponseDto configurationResponseDto = new ConfigurationResponseDto();
        configurationResponseDto.setConditions(getNames(Condition.class));
        configurationResponseDto.setCategories(categoryRepository.findAll());
        configurationResponseDto.setAgeTypes(getNames(AgeType.class));
        configurationResponseDto.setSeasons(getNames(Season.class));
        configurationResponseDto.setProductColors(colorRepository.findAll());
        configurationResponseDto.setProductBrands(brandRepository.findAll());
        configurationResponseDto.setProductGenders(getNames(ProductGender.class));
        configurationResponseDto.setProductMaterials(materialRepository.findAll());
        configurationResponseDto.setProductTags(tagRepository.findAll());
        configurationResponseDto.setProductSizes(sizeRepository.findAll());
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategoryDto> subCategoryDtos = new ArrayList<>();
        for (SubCategory subCategory: subCategories){
            subCategoryDtos.add(new SubCategoryDto(subCategory.getId(), subCategory.getName()));
        }
        configurationResponseDto.setSubCategories(subCategoryDtos);
        configurationResponseDto.setUserGenders(getNames(UserGender.class));
        List<CountryWithCityDto> countryWithCityDtos = new ArrayList<>();
        List<Country> countries = countryRepository.findAll();
        for (Country country:countries){
            CountryWithCityDto countryWithCityDto = new CountryWithCityDto();
            countryWithCityDto.setCountryId(country.getId());
            countryWithCityDto.setCountryName(country.getName());
            List<City> cities = cityRepository.findByCountryId(country.getId());
            List<CityDto> cityDtoList = new ArrayList<>();
            for (City city:cities){
                CityDto cityDto = new CityDto(city.getId(), city.getName());
                cityDtoList.add(cityDto);
            }
            countryWithCityDto.setCityDtoList(cityDtoList);
            countryWithCityDtos.add(countryWithCityDto);
        }
        configurationResponseDto.setCountryWithCityDtos(countryWithCityDtos);
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        responseDto.setMessage("Success");
        responseDto.setPayload(configurationResponseDto);
        return responseDto;
    }

    private static List<String> getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toList();
    }
}
