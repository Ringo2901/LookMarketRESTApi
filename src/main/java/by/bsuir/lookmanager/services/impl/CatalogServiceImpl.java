package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;
import by.bsuir.lookmanager.dto.catalog.mapper.CatalogMapper;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.FavouritesEntity;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.CatalogService;
import com.moesif.api.models.UserBuilder;
import com.moesif.api.models.UserModel;
import com.moesif.servlet.MoesifFilter;
import jakarta.servlet.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CatalogMapper catalogMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FavouritesRepository favouritesRepository;
    @Autowired
    private ProductListMapper productListMapper;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;
    @Autowired
    private ProductSpecification productSpecification;
    @Autowired
    private Filter moesifFilter;
    private static final Logger LOGGER = LogManager.getLogger(CatalogServiceImpl.class);

    @Override
    public ApplicationResponseDto<CatalogResponseDto> addCatalog(Long userId, CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = new ApplicationResponseDto<>();
        Catalog catalogToSave = catalogMapper.catalogRequestToCatalogEntity(requestDto);
        LOGGER.info("Set user for catalog with user id = " + catalogToSave.getUser().getId());
        catalogToSave.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with user id = " + userId + " not found when addCatalog execute!")));
        LOGGER.info("Save catalog to database");
        Catalog catalog = catalogRepository.save(catalogToSave);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
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
        responseDto.setStatus("OK");
        responseDto.setMessage("Catalog add successfully");
        responseDto.setCode(200);
        responseDto.setPayload(catalogMapper.catalogEntityToCatalogResponse(catalog));
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Object> removeCatalog(Long userId, Long catalogId) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find catalog to delete with catalog id = " + catalogId);
        Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog with catalog id = " + catalogId + " not found when removeCatalog execute!"));
        LOGGER.info("Delete catalog with catalog id = " + catalogId);
        catalogRepository.delete(catalog);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
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
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Catalog delete successfully");
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<CatalogResponseDto> updateCatalog(Long userId, Long catalogId, CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find catalog to delete with catalog id = " + catalogId);
        Catalog catalogToUpdate = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog with catalog id = " + catalogId + " not found when updateCatalog execute!"));
        catalogToUpdate.setName(requestDto.getName());
        LOGGER.info("Update catalog with catalog id = " + catalogId + " with new name = " + requestDto.getName());
        Catalog catalog = catalogRepository.save(catalogToUpdate);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
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
        responseDto.setStatus("OK");
        responseDto.setMessage("Catalog add successfully");
        responseDto.setCode(200);
        responseDto.setPayload(catalogMapper.catalogEntityToCatalogResponse(catalog));
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getCatalogInfoByCatalogId(Long catalogId, Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Specification<ProductEntity> spec = productSpecification.byCatalogId(catalogId);
        LOGGER.info("Set pagination for getCatalogInfoByCatalogId");
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        LOGGER.info("Pagination for getCatalogInfoByCatalogId = " + pageable);
        LOGGER.info("Find all products by catalog id = " + catalogId);
        List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(productRepository.findAll(spec, pageable).toList());
        for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
            LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
            generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
            Double price = generalProductResponseDto.getPrice();
            if (price != null) {
                BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                generalProductResponseDto.setPrice(roundedPrice.doubleValue());
            }
        }
        responseDto.setMessage("Catalog found!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        responseDto.setPayload(generalProductResponseDtos);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<CatalogWithItemsDto>> getCatalogsItemsByUserId(Long userId) {
        ApplicationResponseDto<List<CatalogWithItemsDto>> responseDto = new ApplicationResponseDto<>();
        List<CatalogWithItemsDto> catalogWithItemsDto = new ArrayList<>();
        LOGGER.info("Find catalogs by user id = " + userId);
        List<Catalog> catalogs = catalogRepository.findByUserId(userId);
        {
            Catalog personalCatalog = catalogs.get(0);
            LOGGER.info("Find personal catalog by user id = " + userId);
            CatalogWithItemsDto newCatalog = new CatalogWithItemsDto();
            newCatalog.setId(personalCatalog.getId());
            newCatalog.setName(personalCatalog.getName());
            LOGGER.info("Personal catalog for user id = " + userId + " personal catalog without products = " + newCatalog);
            List<GeneralProductResponseDto> generalProducts = productListMapper.toGeneralProductResponseDtoList(productRepository.findFirst2ByCatalogIdOrderByCreatedTimeDesc(personalCatalog.getId()));
            for (GeneralProductResponseDto generalProductResponseDto : generalProducts) {
                LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
                Double price = generalProductResponseDto.getPrice();
                if (price != null) {
                    BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                    generalProductResponseDto.setPrice(roundedPrice.doubleValue());
                }
            }
            newCatalog.setResponseDtoList(generalProducts);
            catalogWithItemsDto.add(newCatalog);
        }

        CatalogWithItemsDto favourites = new CatalogWithItemsDto();
        favourites.setId(0L);
        favourites.setName("Favorites");
        LOGGER.info("Find first 2 favourites by user id = " + userId);
        List<FavouritesEntity> favouritesEntities = favouritesRepository.findFirst2ByUserId(userId);
        List<ProductEntity> favouriteProducts = new ArrayList<>();
        for (FavouritesEntity favouritesEntity : favouritesEntities) {

            favouriteProducts.add(productRepository.findById(favouritesEntity.getProductId()).orElse(null));
        }
        List<GeneralProductResponseDto> favouriteDto = productListMapper.toGeneralProductResponseDtoList(favouriteProducts);
        for (GeneralProductResponseDto generalProductResponseDto : favouriteDto) {
            LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
            generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
            Double price = generalProductResponseDto.getPrice();
            if (price != null) {
                BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                generalProductResponseDto.setPrice(roundedPrice.doubleValue());
            }
        }

        favourites.setResponseDtoList(favouriteDto);

        catalogWithItemsDto.add(favourites);
        for (int i = 1; i < catalogs.size(); i++) {
            LOGGER.info("Find first 2 favourites by user id = " + userId);
            CatalogWithItemsDto newCatalog = new CatalogWithItemsDto();
            newCatalog.setId(catalogs.get(i).getId());
            newCatalog.setName(catalogs.get(i).getName());
            LOGGER.info("Catalog for user id = " + userId + " catalog without products = " + newCatalog);
            List<GeneralProductResponseDto> generalProducts = productListMapper.toGeneralProductResponseDtoList(productRepository.findFirst2ByCatalogIdOrderByCreatedTimeDesc(catalogs.get(i).getId()));
            for (GeneralProductResponseDto generalProductResponseDto : generalProducts) {
                LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
                Double price = generalProductResponseDto.getPrice();
                if (price != null) {
                    BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                    generalProductResponseDto.setPrice(roundedPrice.doubleValue());
                }
            }
            newCatalog.setResponseDtoList(generalProducts);
            catalogWithItemsDto.add(newCatalog);
        }

        responseDto.setMessage("Catalog found!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        responseDto.setPayload(catalogWithItemsDto);
        return responseDto;
    }
}
