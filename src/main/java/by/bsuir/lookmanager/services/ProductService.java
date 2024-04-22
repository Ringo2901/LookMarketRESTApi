package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long userId, Long id);

    ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsByCategory(Long userId, String sex, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsWithSorting(Long userId, String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder,
                                                                                   List<String> size, List<String> color, List<String> brand, List<String> filtSeason, List<String> filtGender,
                                                                                   List<String> filtAgeType, List<String> tags, List<String> materials, List<String> subcategory, List<String> category,
                                                                                   Double minPrice, Double maxPrice) throws SQLException;
    ApplicationResponseDto<Long> saveProduct(ProductDetailsRequestDto requestDto);

    ApplicationResponseDto<ProductDetailsResponseDto> updateProduct(Long userId, Long id, ProductInformationRequestDto requestDto);

    ApplicationResponseDto<Object> deleteProduct(Long id);
}
