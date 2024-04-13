package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import by.bsuir.lookmanager.services.ProductService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtValidator jwtValidator;

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> getProductById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.getProductInformationById(jwtValidator.validateTokenAndGetUserId(token), id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getProducts(@RequestHeader("Authorization") String token, @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                                               @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = productService.getProducts(jwtValidator.validateTokenAndGetUserId(token), pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/by-category/{id}")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getProductsByCategory(@RequestHeader("Authorization") String token, @PathVariable Long id,
                                                                                                         @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                         @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                                                         @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = productService.getProductsByCategory(jwtValidator.validateTokenAndGetUserId(token), id, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/sorting")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getProductsWithSorting(@RequestHeader("Authorization") String token, @RequestParam(required = false) String query,
                                                                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                          @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                                                          @RequestParam(required = false, defaultValue = "desc") String sortOrder,
                                                                                                          @RequestParam(required = false) List<Integer> sizes,
                                                                                                          @RequestParam(required = false) List<String> colors,
                                                                                                          @RequestParam(required = false) List<String> brand,
                                                                                                          @RequestParam(required = false) List<String> seasons,
                                                                                                          @RequestParam(required = false) List<String> genders,
                                                                                                          @RequestParam(required = false) List<String> ageTypes,
                                                                                                          @RequestParam(required = false) List<String> tags,
                                                                                                          @RequestParam(required = false) List<String> materials,
                                                                                                          @RequestParam(required = false) List<String> subcategory,
                                                                                                          @RequestParam(required = false) List<String> category,
                                                                                                          @RequestParam(required = false, defaultValue = "0") Double minPrice,
                                                                                                          @RequestParam(required = false, defaultValue = "1000") Double maxPrice) throws SQLException {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = productService.getProductsWithSorting(jwtValidator.validateTokenAndGetUserId(token), query, pageSize, ++pageNumber, sortBy, sortOrder, sizes, colors, brand, seasons, genders, ageTypes, tags, materials, subcategory, category, minPrice, maxPrice);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> saveProduct(@RequestBody ProductDetailsRequestDto requestDto) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.saveProduct(requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> updateProductInformation(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody ProductInformationRequestDto requestDto) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.updateProduct(jwtValidator.validateTokenAndGetUserId(token), id, requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteProduct(@PathVariable Long id) {
        ApplicationResponseDto<Object> responseDto = productService.deleteProduct(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

}
