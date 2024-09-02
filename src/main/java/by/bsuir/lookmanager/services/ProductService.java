package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.ListResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long userId, Long id, String lang);

    ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProducts(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws SQLException;

    ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProductsByCategory(Long userId, String sex, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws SQLException;

    ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProductsWithSorting(Long userId, String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder,
                                                                                   List<Integer> size, List<Integer> color, List<Integer> brand, List<Integer> filtSeason, List<Integer> filtGender,
                                                                                   List<Integer> filtAgeType, List<Integer> tags, List<Integer> materials, List<Integer> subcategory, List<Integer> category,
                                                                                   Double minPrice, Double maxPrice) throws SQLException;
    ApplicationResponseDto<Long> saveProduct(ProductDetailsRequestDto requestDto);

    ApplicationResponseDto<ProductDetailsResponseDto> updateProduct(Long userId, Long id, ProductInformationRequestDto requestDto);

    ApplicationResponseDto<Object> deleteProduct(Long id);
}
