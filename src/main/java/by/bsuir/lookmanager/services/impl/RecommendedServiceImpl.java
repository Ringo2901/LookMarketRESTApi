package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.exceptions.BadParameterValueException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.recomended.ProductSimilarityCalculator;
import by.bsuir.lookmanager.services.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> findRecommendedProducts(Long userId, Long numberOfRecommendedItems) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Pageable pageable = PageRequest.of(0, 100, Sort.by("updateTime").descending());
        List<ProductEntity> responseEntityList = productRepository.findAll(pageable).toList();
        List<ProductEntity> favouriteProducts = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found!")).getFavouriteProducts();
        Map<ProductEntity, Double> similarityMap = new HashMap<>();
        for (ProductEntity product : responseEntityList) {
            similarityMap.put(product, 0.0);
        }
        try {
            productSimilarityCalculator.initializeIndex(responseEntityList);
            for (ProductEntity favouriteProduct : favouriteProducts) {
                for (ProductEntity product : similarityMap.keySet()) {
                    similarityMap.put(product, similarityMap.get(product) + productSimilarityCalculator.calculateSimilarity(favouriteProduct, product));
                }
            }
        } catch (IOException e) {
            throw new BadParameterValueException("Some troubles!");
        }
        List<ProductEntity> topNKeys = similarityMap.entrySet().stream()
                .sorted(Map.Entry.<ProductEntity, Double>comparingByValue().reversed())
                .limit(numberOfRecommendedItems)
                .map(Map.Entry::getKey)
                .toList();
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product delete!");
        List<GeneralProductResponseDto> responseDtos = productResponseMapper.toGeneralProductResponseDtoList(topNKeys);
        for (GeneralProductResponseDto generalProductResponseDto : responseDtos) {
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageData(imageDataResponseDto == null ? null : imageDataResponseDto.getImageData());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
        }
        responseDto.setPayload(responseDtos);
        return responseDto;
    }
}
