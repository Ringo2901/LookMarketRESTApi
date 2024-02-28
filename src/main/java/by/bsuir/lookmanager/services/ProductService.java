package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;

import java.util.List;

public interface ProductService {
    ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long id);
    ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
