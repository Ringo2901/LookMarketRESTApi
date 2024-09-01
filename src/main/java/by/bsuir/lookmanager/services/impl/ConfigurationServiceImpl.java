package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.configuration.*;
import by.bsuir.lookmanager.entities.product.information.*;
import by.bsuir.lookmanager.entities.user.information.City;
import by.bsuir.lookmanager.entities.user.information.Country;
import by.bsuir.lookmanager.entities.user.information.UserGender;
import by.bsuir.lookmanager.services.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private AgeTypeRepository ageTypeRepository;
    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private ProductGenderRepository productGenderRepository;
    @Autowired
    private ProductStatusRepository productStatusRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private UserGenderRepository userGenderRepository;
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);
    @Override
    public ApplicationResponseDto<ConfigurationResponseDto> getConfiguration(String lang) {
        LOGGER.info("Get configuration");
        ApplicationResponseDto<ConfigurationResponseDto> responseDto = new ApplicationResponseDto<>();
        ConfigurationResponseDto configurationResponseDto = new ConfigurationResponseDto();

        LOGGER.info("Get enums config");
        configurationResponseDto.setConditions(conditionRepository.findAll().stream()
                .map(condition -> new EnumDto(Math.toIntExact(condition.getId()),
                        "ru".equals(lang) ? condition.getNameRu() : condition.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setCategories(categoryRepository.findAll().stream()
                .map(category -> new EnumDto(Math.toIntExact(category.getId()),
                        "ru".equals(lang) ? category.getNameRu() : category.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setAgeTypes(ageTypeRepository.findAll().stream()
                .map(ageType -> new EnumDto(Math.toIntExact(ageType.getId()),
                        "ru".equals(lang) ? ageType.getNameRu() : ageType.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setSeasons(seasonRepository.findAll().stream()
                .map(season -> new EnumDto(Math.toIntExact(season.getId()),
                        "ru".equals(lang) ? season.getNameRu() : season.getNameEn()))
                .collect(Collectors.toList()));

        LOGGER.info("Get lists config");
        configurationResponseDto.setProductColors(colorRepository.findAll().stream()
                .map(color -> new EnumDto(Math.toIntExact(color.getId()),
                        "ru".equals(lang) ? color.getNameRu() : color.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setProductBrands(brandRepository.findAll());

        configurationResponseDto.setProductGenders(productGenderRepository.findAll().stream()
                .map(gender -> new EnumDto(Math.toIntExact(gender.getId()),
                        "ru".equals(lang) ? gender.getNameRu() : gender.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setProductMaterials(materialRepository.findAll().stream()
                .map(material -> new EnumDto(Math.toIntExact(material.getId()),
                        "ru".equals(lang) ? material.getNameRu() : material.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setProductTags(tagRepository.findAll().stream()
                .map(tag -> new EnumDto(Math.toIntExact(tag.getId()),
                        "ru".equals(lang) ? tag.getNameRu() : tag.getNameEn()))
                .collect(Collectors.toList()));

        configurationResponseDto.setProductSizes(sizeRepository.findAll());

        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategoryDto> subCategoryDtos = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            subCategoryDtos.add(new SubCategoryDto(subCategory.getId(),
                    "ru".equals(lang) ? subCategory.getNameRu() : subCategory.getNameEn()));
        }
        configurationResponseDto.setSubCategories(subCategoryDtos);

        configurationResponseDto.setUserGenders(userGenderRepository.findAll().stream()
                .map(gender -> new EnumDto(Math.toIntExact(gender.getId()),
                        "ru".equals(lang) ? gender.getNameRu() : gender.getNameEn()))
                .collect(Collectors.toList()));

        List<CountryWithCityDto> countryWithCityDtos = new ArrayList<>();
        List<Country> countries = countryRepository.findAll();
        LOGGER.info("Get countries and cities config");
        for (Country country : countries) {
            CountryWithCityDto countryWithCityDto = new CountryWithCityDto();
            countryWithCityDto.setCountryId(country.getId());
            countryWithCityDto.setCountryName("ru".equals(lang) ? country.getNameRu() : country.getNameEn());

            List<City> cities = cityRepository.findByCountryId(country.getId());
            List<CityDto> cityDtoList = new ArrayList<>();
            for (City city : cities) {
                CityDto cityDto = new CityDto(city.getId(),
                        "ru".equals(lang) ? city.getNameRu() : city.getNameEn());
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
}
