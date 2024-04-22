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
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.exceptions.AlreadyExistsException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.FavoritesService;
import com.moesif.api.models.UserBuilder;
import com.moesif.api.models.UserModel;
import com.moesif.servlet.MoesifFilter;
import jakarta.servlet.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Component
@CacheConfig(cacheNames = "recommendedProducts")
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductListMapper productListMapper;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private Filter moesifFilter;
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getFavoritesByUserId(Long id, Integer pageNumber, Integer pageSize) throws NotFoundException {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find user by user id = " + id);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with user id = " + id + " not found when getFavoritesByUserId execute!"));
        LOGGER.info("Get favourite products for user with user id = " + id);
        List<ProductEntity> favoriteProducts = user.getFavouriteProducts();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, favoriteProducts.size());
        if (startIndex > endIndex){
            LOGGER.warn("Products with favourites not found, pagination corrupted ");
            throw new NotFoundException("Products not found!");
        }
        responseDto.setMessage("User found!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        LOGGER.info("Get favourites products for user with ud = " + id + " and start index = " + startIndex + " and end index = " + endIndex);
        List<ProductEntity> paginatedFavorites = favoriteProducts.subList(startIndex, endIndex);
        List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(paginatedFavorites);
        for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
            LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
            generalProductResponseDto.setFavourite(true);
        }
        responseDto.setPayload(generalProductResponseDtos);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> addFavorite(Long userId, Long productId) throws NotFoundException, AlreadyExistsException{
        LOGGER.info("Find user with id = " + userId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found when addFavorite execute!"));
        LOGGER.info("Find product with id = " + productId);
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with id = " + productId + " not found when addFavorite execute!"));
        if (user.getFavouriteProducts().contains(product)) {
            throw new AlreadyExistsException("Product with id = " + productId + " already in favourites for user with id = " + userId + " when addFavorite execute");
        }
        String keyPattern = "recommendedProducts::" + userId + "_*";
        Set<String> keysToDelete = redisTemplate.keys(keyPattern);
        if (keysToDelete != null) {
            redisTemplate.delete(keysToDelete);
        }
        LOGGER.info("Deleted keys from cache: " + keysToDelete);
        LOGGER.info("Add product with product id = " + productId + " to favourites for user with user id = " + userId);
        user.getFavouriteProducts().add(product);
        userRepository.save(user);
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        responseDto.setMessage("Product add to favourites!");
        responseDto.setStatus("OK");
        responseDto.setCode(201);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> deleteFavorite(Long userId, Long productId) throws NotFoundException, AlreadyExistsException{
        LOGGER.info("Find user with id = " + userId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found when deleteFavorite execute!"));
        LOGGER.info("Find product with id = " + productId);
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with id = " + productId + " not found when deleteFavorite execute!"));

        if (!user.getFavouriteProducts().contains(product)) {
            throw new AlreadyExistsException("Product with id = " + productId + " not in favourites for user with id = " + userId + " when deleteFavorite execute");
        }
        String keyPattern = "recommendedProducts::" + userId + "_*";
        Set<String> keysToDelete = redisTemplate.keys(keyPattern);
        if (keysToDelete != null) {
            redisTemplate.delete(keysToDelete);
        }
        LOGGER.info("Deleted keys from cache: " + keysToDelete);
        LOGGER.info("Remove product with product id = " + productId + " from favourites for user with user id = " + userId);
        user.getFavouriteProducts().remove(product);
        userRepository.save(user);
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        responseDto.setMessage("Product remove to favourites!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }
}
