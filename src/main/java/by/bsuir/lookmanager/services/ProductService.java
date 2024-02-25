package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.ProductResponseDto;

public interface ProductService {
    ApplicationResponseDto<ProductResponseDto> getProductInformationById(Long id);
}
