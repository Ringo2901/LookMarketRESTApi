package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.CatalogRepository;
import by.bsuir.lookmanager.dao.FavouritesRepository;
import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;
import by.bsuir.lookmanager.dto.catalog.mapper.CatalogMapper;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.GeneralProductResponseMapper;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ApplicationResponseDto<CatalogWithItemsDto> getCatalogInfoByCatalogId(Long catalogId) {
        ApplicationResponseDto<CatalogWithItemsDto> responseDto = new ApplicationResponseDto<>();
        CatalogWithItemsDto catalogWithItemsDto = new CatalogWithItemsDto();
        Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog not found!"));
        catalogWithItemsDto.setId(catalogId);
        catalogWithItemsDto.setName(catalog.getName());
        List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(productRepository.findByCatalogId(catalogId));
        for (GeneralProductResponseDto generalProductResponseDto: generalProductResponseDtos){
            generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(catalog.getUser().getId(), generalProductResponseDto.getId()));
        }
        catalogWithItemsDto.setResponseDtoList(generalProductResponseDtos);
        responseDto.setMessage("Catalog found!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        responseDto.setPayload(catalogWithItemsDto);
        return responseDto;
    }
}
