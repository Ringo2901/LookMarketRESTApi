package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.FavouritesRepository;
import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.exceptions.BadParameterValueException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.recomended.ProductSimilarityCalculator;
import by.bsuir.lookmanager.services.RecommendedService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@CacheConfig(cacheNames = "recommendedProducts")
public class RecommendedServiceImpl implements RecommendedService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductListMapper productResponseMapper;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;
    @Autowired
    private ProductSimilarityCalculator productSimilarityCalculator;
    @Autowired
    private FavouritesRepository favouritesRepository;
    @Autowired
    private ProductSpecification productSpecification;
    private static final Logger LOGGER = LogManager.getLogger(RecommendedServiceImpl.class);

    @Override
    @Cacheable(value = "recommendedProducts", key = "#userId + '_' + #pageNumber + '_' + #pageSize")
    public ApplicationResponseDto<List<GeneralProductResponseDto>> findRecommendedProducts(Long userId, Integer pageNumber, Integer pageSize) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();

        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdTime").descending());
        LOGGER.info("Get pageable for recommended = " + pageable);
        Specification<ProductEntity> spec = productSpecification.byUserId(userId);
        List<ProductEntity> responseEntityList = productRepository.findAll(spec, pageable).toList();

        List<ProductEntity> filteredList = responseEntityList.stream()
                .filter(product -> !favouritesRepository.existsByUserIdAndProductId(userId, product.getId()))
                .toList();

        LOGGER.info("Find favourites product for user with id = " + userId);
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found!");
        }
        List<ProductEntity> favouriteProducts = user.getFavouriteProducts();
        Map<ProductEntity, Double> similarityMap = new HashMap<>();
        LOGGER.info("Create similarity map");
        for (ProductEntity product : filteredList) {
            similarityMap.put(product, 0.0);
        }
        try {
            productSimilarityCalculator.initializeIndex(filteredList);
            LOGGER.info("Calculate similarity");
            if (favouriteProducts != null) {
                for (ProductEntity favouriteProduct : favouriteProducts) {
                    for (ProductEntity product : similarityMap.keySet()) {
                        similarityMap.put(product, similarityMap.get(product) + productSimilarityCalculator.calculateSimilarity(favouriteProduct, product));
                    }
                }
                for (ProductEntity product : similarityMap.keySet()) {
                    LOGGER.info("Total similarity for user with id = " + userId + " for product with id = " + product.getId() + " and title = " + product.getTitle() + " is " + similarityMap.get(product));
                }
            }
        } catch (IOException e) {
            throw new BadParameterValueException("Some troubles!");
        }
        List<ProductEntity> topNKeys;
        if (favouriteProducts != null) {
            topNKeys = similarityMap.entrySet().stream()
                    .sorted(Map.Entry.<ProductEntity, Double>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .toList();
        } else {
            topNKeys = filteredList;
        }
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, topNKeys.size());

        if (startIndex > endIndex) {
            LOGGER.warn("Products with recommended not found, pagination corrupted ");
            throw new NotFoundException("Products not found!");
        }
        LOGGER.info("Get recommended from " + startIndex + " to " + endIndex);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Recommended found!");
        List<GeneralProductResponseDto> responseDtos = productResponseMapper.toGeneralProductResponseDtoList(topNKeys.subList(startIndex, endIndex));
        for (GeneralProductResponseDto generalProductResponseDto : responseDtos) {
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            generalProductResponseDto.setFavourite(false);
            Double price = generalProductResponseDto.getPrice();
            if (price != null) {
                BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                generalProductResponseDto.setPrice(roundedPrice.doubleValue());
            }
        }
        responseDto.setPayload(responseDtos);
        return responseDto;
    }
}
