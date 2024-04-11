package by.bsuir.lookmanager.dto.catalog.mapper;

import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    Catalog catalogRequestToCatalogEntity (CatalogRequestDto requestDto);
    CatalogResponseDto catalogEntityToCatalogResponse (Catalog catalog);
}
