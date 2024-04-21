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
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.FavouritesEntity;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    @Override
    public ApplicationResponseDto<CatalogResponseDto> addCatalog(Long userId, CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = new ApplicationResponseDto<>();
        Catalog catalogToSave = catalogMapper.catalogRequestToCatalogEntity(requestDto);
        catalogToSave.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!")));
        Catalog catalog = catalogRepository.save(catalogToSave);
        responseDto.setStatus("OK");
        responseDto.setMessage("Catalog add successfully");
        responseDto.setCode(200);
        responseDto.setPayload(catalogMapper.catalogEntityToCatalogResponse(catalog));
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Object> removeCatalog(Long userId, Long catalogId) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog not found!"));
        catalogRepository.delete(catalog);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Catalog delete successfully");
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<CatalogResponseDto> updateCatalog(Long userId, Long catalogId, CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = new ApplicationResponseDto<>();
        Catalog catalogToUpdate = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog not found!"));
        catalogToUpdate.setName(requestDto.getName());
        Catalog catalog = catalogRepository.save(catalogToUpdate);
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
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(productRepository.findAll(spec, pageable).toList());
        for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
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


        List<Catalog> catalogs = catalogRepository.findByUserId(userId);
        {
            Catalog personalCatalog = catalogs.get(0);
            CatalogWithItemsDto newCatalog = new CatalogWithItemsDto();
            newCatalog.setId(personalCatalog.getId());
            newCatalog.setName(personalCatalog.getName());
            List<GeneralProductResponseDto> generalProducts = productListMapper.toGeneralProductResponseDtoList(productRepository.findFirst2ByCatalogIdOrderByCreatedTimeDesc(personalCatalog.getId()));
            for (GeneralProductResponseDto generalProductResponseDto : generalProducts) {
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(personalCatalog.getUser().getId(), generalProductResponseDto.getId()));
            }
            newCatalog.setResponseDtoList(generalProducts);
            catalogWithItemsDto.add(newCatalog);
        }

        CatalogWithItemsDto favourites = new CatalogWithItemsDto();
        favourites.setId(0L);
        favourites.setName("Favorites");
        List<FavouritesEntity> favouritesEntities = favouritesRepository.findFirst2ByUserId(userId);
        List<ProductEntity> favouriteProducts = new ArrayList<>();
        for (FavouritesEntity favouritesEntity : favouritesEntities) {

            favouriteProducts.add(productRepository.findById(favouritesEntity.getProductId()).orElse(null));
        }
        List<GeneralProductResponseDto> favouriteDto = productListMapper.toGeneralProductResponseDtoList(favouriteProducts);
        for (GeneralProductResponseDto generalProductResponseDto : favouriteDto) {
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
            generalProductResponseDto.setFavourite(true);
        }

        favourites.setResponseDtoList(favouriteDto);

        catalogWithItemsDto.add(favourites);
        for (int i = 1; i < catalogs.size(); i++) {
            CatalogWithItemsDto newCatalog = new CatalogWithItemsDto();
            newCatalog.setId(catalogs.get(i).getId());
            newCatalog.setName(catalogs.get(i).getName());
            List<GeneralProductResponseDto> generalProducts = productListMapper.toGeneralProductResponseDtoList(productRepository.findFirst2ByCatalogIdOrderByCreatedTimeDesc(catalogs.get(i).getId()));
            for (GeneralProductResponseDto generalProductResponseDto : generalProducts) {
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(catalogs.get(i).getUser().getId(), generalProductResponseDto.getId()));
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
