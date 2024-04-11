package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;

public interface CatalogService {
    ApplicationResponseDto<CatalogResponseDto> addCatalog(Long userId, CatalogRequestDto requestDto);
    ApplicationResponseDto<Object> removeCatalog(Long userId, Long catalogId);
    ApplicationResponseDto<CatalogResponseDto> updateCatalog(Long userId, Long catalogId, CatalogRequestDto requestDto);
    ApplicationResponseDto<CatalogWithItemsDto> getCatalogInfoByCatalogId (Long catalogId);
}
